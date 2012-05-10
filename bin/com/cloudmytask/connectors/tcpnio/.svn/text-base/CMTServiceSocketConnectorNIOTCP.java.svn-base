package com.cloudmytask.connectors.tcpnio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.cloudmytask.service.CMTPublicServiceInterface;
import com.cloudmytask.utils.DataUtils;

public class CMTServiceSocketConnectorNIOTCP extends Thread implements NIOTCPSocketConnector {
	public static final int NumberOfThreadsInPool = 3;
	
	private int ports[];
	private boolean running;
	private Selector selector;
	
	private Hashtable<SelectionKey, ArrayList<byte[]>> writeBuffers;
	private Hashtable<SelectionKey, byte[]> readBuffers;
	private LinkedList<NIOTCPChangeRequest> changeRequestQueue;

	private ArrayList<ServerSocketChannel> serverChannels;
	private ArrayList<SocketChannel> socketChannels;

	private ExecutorService threadPool;
	private CMTPublicServiceInterface sobj;
	
	private LinkedBlockingQueue<ByteBuffer> bufferPool;
	
	public CMTServiceSocketConnectorNIOTCP(int ports[], CMTPublicServiceInterface sobj) {
		this.ports = ports;
		this.sobj = sobj;
		
		try {
			this.selector = SelectorProvider.provider().openSelector();
		} catch (Exception e) {
			System.err.println("exceptie la crearea Selector-ului: " + e);
			e.printStackTrace();
		}

		this.writeBuffers = new Hashtable<SelectionKey, ArrayList<byte[]>>();
		this.readBuffers = new Hashtable<SelectionKey, byte[]>();
		this.changeRequestQueue = new LinkedList<NIOTCPChangeRequest>();

		this.serverChannels = new ArrayList<ServerSocketChannel>();
		this.socketChannels = new ArrayList<SocketChannel>();

		this.threadPool = Executors.newFixedThreadPool(NumberOfThreadsInPool);
		this.bufferPool = new LinkedBlockingQueue<ByteBuffer>();
		
		for (int i = 0; i < NumberOfThreadsInPool; i++) {
			this.bufferPool.add(ByteBuffer.allocate(8192));
		}
	}

	public synchronized void startRunning() {
		this.running = true;
		this.start();
	}
	
	public synchronized void stopRunning() {
		this.running = false;
		this.threadPool.shutdown();
		this.selector.wakeup();
	}
	
	protected synchronized boolean isRunning() {
		return this.running;
	}
	
	public void run() {
		System.out.println("[NIOTCPServer] Am pornit");
		
		// Creeaza ServerSocketChannels.		
		for (int i = 0; i < this.ports.length; i++) {
			try {
				ServerSocketChannel serverChannel = ServerSocketChannel.open();
				serverChannel.configureBlocking(false);
				InetSocketAddress isa = new InetSocketAddress("127.0.0.1", this.ports[i]);
				serverChannel.socket().bind(isa);
				serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
				this.serverChannels.add(serverChannel);
			} catch (Exception e) {
				System.err.println("Exceptie la crearea ServerSocketChannel-ului: " + e);
				e.printStackTrace();
			}
		}

		while (this.isRunning()) {
			try {
				this.selector.select(10000);
				
				// Proceseaza cheile pentru care s-au produs evenimente.
				Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					if (key.isAcceptable()) {
						System.out.println("[NIOTCPServer] Eveniment de accept la cheia " + key);
						key.interestOps(key.interestOps() ^ SelectionKey.OP_ACCEPT);
						this.threadPool.submit(new AcceptJob(this, key));						
					}
					
					if (key.isWritable()) {
						System.out.println("[NIOTCPServer] Eveniment de write_posibil la cheia " + key);
						key.interestOps(key.interestOps() ^ SelectionKey.OP_WRITE);
						this.threadPool.submit(new WriteJob(this, key));
					}

					if (key.isReadable()) {
						System.out.println("[NIOTCPServer] Eveniment de read la cheia " + key);
						key.interestOps(key.interestOps() ^ SelectionKey.OP_READ);
						this.threadPool.submit(new ReadJob(this, key));
					}					
				}
								
				// Proceseaza cererile de schimbare a operatiilor de interes.
				NIOTCPChangeRequest creq;				
				synchronized (this.changeRequestQueue) {
					while ((creq = this.changeRequestQueue.poll()) != null) {
						if (creq.socketChannel == null) {
							System.out.println("[NIOTCPServer] Schimb operatiile cheii " + creq.key + " la " + creq.newOps);
							creq.key.interestOps(creq.newOps);
						} else {
							System.out.println("[NIOTCPServer] Inregistrez un socket nou la Selector");
							creq.socketChannel.register(this.selector, SelectionKey.OP_READ);
						}
					}
				}
			} catch (Exception e) {
				System.err.println("Exceptie in thread-ul Selectorului: " + e);
				e.printStackTrace();
			}
		}
		
		// Inchide toate ServerSocketChannel-urile.
		for (ServerSocketChannel ssc: this.serverChannels) {
			try {
				ssc.close();
			} catch (Exception e) {}
		}
		
		// Asteapta ca toate job-urile din thread pool sa se termine.
		try {
			this.threadPool.awaitTermination(100000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {}
		
		// Inchide toate SocketChannel-urile.
		for (SocketChannel schan: this.socketChannels) {
			try {
				schan.close();
			} catch (Exception e) {}
		}

		System.out.println("[NIOTCPServer] M-am oprit");
	}
	
	protected void doAccept(SelectionKey key) throws Exception {
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		synchronized (this.socketChannels) {
			this.socketChannels.add(socketChannel);
		}
		
		synchronized (this.changeRequestQueue) {
			this.changeRequestQueue.add(new NIOTCPChangeRequest(key, SelectionKey.OP_ACCEPT));
			this.changeRequestQueue.add(new NIOTCPChangeRequest(socketChannel));
		}
				
		this.selector.wakeup();
	}
	
	protected void doRead(SelectionKey key) throws Exception {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		ByteBuffer rBuffer = this.bufferPool.take();
		rBuffer.clear();

		int numRead;
		
		try {
			numRead = socketChannel.read(rBuffer);
		} catch (Exception e) {
			numRead = -1000000000;
		}

		//System.out.println("numRead=" + numRead);		
		
		if (numRead <= 0) {
			System.out.println("[NIOTCPServer] S-a inchis socket-ul asociat cheii " + key);
			key.channel().close();
			key.cancel();
			this.bufferPool.add(rBuffer);
			return;
		}

		byte[] rbuf = null;
		rbuf = this.readBuffers.get(key);
		
		int rbuflen = 0;
		if (rbuf != null) {
			rbuflen = rbuf.length;
		}
		
		byte[] currentBuf = rBuffer.array();
		System.out.println("[NIOTCPServer] S-au citit " + numRead + " bytes de pe socket-ul asociat cheii " + key + " : " + currentBuf);

		byte[] newBuf = new byte[rbuflen + numRead];
		
		// Copiaza datele primite in newBuf (rbuf sunt datele primite
		// anterior si care nu formeaza o cerere completa, iar currentBuf
		// contine datele primite la read-ul curent.
		for (int i = 0; i < rbuflen; i++) {
			newBuf[i] = rbuf[i];
		}
		
		for (int i = rbuflen; i < newBuf.length; i++) {
			newBuf[i] = currentBuf[i - rbuflen];
		}
		int ln=0;
		

		for(int i=0;i<newBuf.length;i++)
			System.out.print(newBuf[i] +" ");
		
		System.out.println("newBuff " + newBuf.length + " l n " + ln);
		
		int i=0;
		byte[] request;
		while ((request = DataUtils.getCompleteRequest(newBuf)) != null) {
			
			// Actualizeaza newBuf.
			
			
			byte[] newBufAux = new byte[newBuf.length - request.length];
			for (i = 0; i < newBufAux.length; i++) {
				newBufAux[i] = newBuf[i + request.length];
			}
			System.out.println("aaaaaaa   newBuffAux " + newBufAux.length);
			newBuf = newBufAux;
			this.sobj.decodeRequest(request, new SocketConnectorNIOTCPCallbackObject(this, key));
		}
		
		//reface bufferul pentru partea de cerere care nu a fost luata complet
		/*
		byte[] finalBuf = null;
		if (i > 0) {
			finalBuf = new byte[newBuf.length - i];
			for (int j = i; j < newBuf.length; j++) {
				finalBuf[j - i] = newBuf[j];
			}
		} else {
			finalBuf = newBuf;
		}
		*/
		
		this.readBuffers.put(key, newBuf);

		synchronized (this.changeRequestQueue) {
			this.changeRequestQueue.add(new NIOTCPChangeRequest(key, key.interestOps() | SelectionKey.OP_READ));			
		}
		
		this.bufferPool.add(rBuffer);
		this.selector.wakeup();
		
	}

	protected void doWrite(SelectionKey key) throws Exception {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		ByteBuffer wBuffer = this.bufferPool.take();
		
		ArrayList<byte[]> wbuf = null;
		
		synchronized (key) {
			wbuf = this.writeBuffers.get(key);

			while (wbuf.size() > 0) {
				byte[] bbuf = wbuf.get(0);
				wbuf.remove(0);
				
				wBuffer.clear();
				wBuffer.put(bbuf);
				wBuffer.flip();
	
				int numWritten = socketChannel.write(wBuffer);
				System.out.println("[NIOTCPServer] Am scris " + numWritten + " bytes pe socket-ul asociat cheii " + key);
	
				if (numWritten < bbuf.length) {
					byte[] newBuf = new byte[bbuf.length - numWritten];
	
					// Copiaza datele inca nescrise din bbuf in newBuf.
					for (int i = numWritten; i < bbuf.length; i++) {
						newBuf[i - numWritten] = bbuf[i];
					}
					
					wbuf.add(0, newBuf);
				}
			}
		
			if (wbuf.size() > 0) {
				synchronized (this.changeRequestQueue) {
					this.changeRequestQueue.add(new NIOTCPChangeRequest(key, key.interestOps() | SelectionKey.OP_WRITE));
				}

				this.selector.wakeup();
			}
		}
		
		this.bufferPool.add(wBuffer);
	}

	// Metoda apelata de toate obiectele CallbackObject pentru a trimite rezultatele inapoi.
	public void sendData(SelectionKey key, byte[] data) {
		System.out.println("[NIOTCPServer] Se doreste scrierea a " + data.length + " bytes pe socket-ul asociat cheii " + key);
		
		ArrayList<byte[]> wbuf = null;
		
		synchronized (key) {
			wbuf = this.writeBuffers.get(key);
			if (wbuf == null) {
				wbuf = new ArrayList<byte[]>();
				this.writeBuffers.put(key, wbuf);
			}

			wbuf.add(data);
			synchronized (this) {
				this.changeRequestQueue.add(new NIOTCPChangeRequest(key, SelectionKey.OP_READ | SelectionKey.OP_WRITE));
			}
		}
				
		this.selector.wakeup();
	}

}
