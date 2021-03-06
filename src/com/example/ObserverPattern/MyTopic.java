package com.example.ObserverPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class MyTopic implements Subject {
	private List<Observer> observers;
	private String message;
	private boolean changed;
	private final Object MUTEX = new Object();

	public MyTopic() {
		this.observers = new ArrayList<>();
	}

	@Override
	public void register(com.example.ObserverPattern.Observer obj1) {
		if (obj1 == null)
			throw new NullPointerException("Null Observer");
		synchronized (MUTEX) {
			if (!observers.contains(obj1))
				observers.add((Observer) obj1);
		}
	}

	@Override
	public void unregister(Observer obj) {
		synchronized (MUTEX) {
			observers.remove(obj);
		}
	}

	@Override
	public void notifyObservers() {
		List<Observer> observersLocal = null;
		// synchronization is used to make sure any observer registered after
		// message is received is not notified
		synchronized (MUTEX) {
			if (!changed)
				return;
			observersLocal = new ArrayList<>(this.observers);
			this.changed = false;
		}
		for (Observer obj : observersLocal) {
			obj.update(null, MUTEX);
		}

	}

	@Override
	public Object getUpdate(Observer obj) {
		return this.message;
	}

	// method to post message to the topic
	public void postMessage(String msg) {
		System.out.println("Message Posted to Topic:" + msg);
		this.message = msg;
		this.changed = true;
		notifyObservers();
	}

}
