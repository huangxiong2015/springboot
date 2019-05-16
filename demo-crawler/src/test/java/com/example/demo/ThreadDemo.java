package com.example.demo;

/**
 * 死锁demo
 * @author hengz
 *
 */
public class ThreadDemo {
	private final Object r = new Object();
	private final Object w = new Object();
	//private Queue<Object> queue = new LinkedBlockingQueue<>();
	class WriteThread extends Thread{
			public void run() {
				synchronized (w) {
					try {
						sleep(2000);
						synchronized (r) {
							System.out.println("me is write");
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
	}
	
	class  ReadThread extends Thread{
		public void run() {
			synchronized (r) {
				try {
					sleep(2000);
					synchronized (w) {
						System.out.println("me is read");
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	public static void main(String[] args) {
		ThreadDemo demo = new ThreadDemo();
		ReadThread r = demo.new ReadThread();
		WriteThread w = demo.new WriteThread();
		r.start();
		w.start();
		while(true);
	}
}

