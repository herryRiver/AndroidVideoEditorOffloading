package com.cs4911.video_editor.android;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.nio.ByteBuffer;

import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;

import com.cs4911.video_editor.testproto.TestProtocol;

import android.util.Log;

public class CloudClient implements Runnable {
	
	final String serverIP = "143.215.204.52";
	final static int port = 20001;
	NettyTransceiver transceiver;
	TestProtocol.Callback client;
	
	BandwidthMeasurement bandwidth;
	
	@Override
	public void run() {
		try {
			InitializeClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void InitializeClient() throws IOException {
		
		Log.v("CloudClient", "Initializing client");
		bandwidth = new BandwidthMeasurement(serverIP);
		
		InetAddress serverAddr = null;
		
		try {
			serverAddr  = InetAddress.getByName(serverIP);
			transceiver = new NettyTransceiver(new InetSocketAddress(serverAddr, port));
			client      = SpecificRequestor.getClient(TestProtocol.Callback.class, transceiver);
			Log.v("CloudClient", "Connecting to server " + serverIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void addBlurEffect() throws org.apache.avro.AvroRemoteException {
    	client.addBlurEffect();
    }

    public void addColorSaturationEffect() throws org.apache.avro.AvroRemoteException {
    	client.addColorSaturationEffect();
    }

    public void addDrawingEffect() throws org.apache.avro.AvroRemoteException {
    	client.addDrawingEffect();
    }

    public void addEdgeDetectionEffect() throws org.apache.avro.AvroRemoteException {
    	client.addEdgeDetectionEffect();
    }

    public void addGradientMagnitudeEffect() throws org.apache.avro.AvroRemoteException {
    	client.addGradientMagnitudeEffect();
    }

    public void addGrayscaleEffect() throws org.apache.avro.AvroRemoteException {
    	client.addGrayscaleEffect();
    }

    public void addHorizontalFlipEffect() throws org.apache.avro.AvroRemoteException {
    	client.addHorizontalFlipEffect();
    }

    public void addHoughCircleEffect() throws org.apache.avro.AvroRemoteException {
    	client.addHoughCircleEffect();
    }

    public void addHoughLineEffect() throws org.apache.avro.AvroRemoteException {
    	client.addHoughLineEffect();
    }

    public void addIdentityEffect() throws org.apache.avro.AvroRemoteException {
    	client.addIdentityEffect();
    }

    public void addMotionHistoryEffect() throws org.apache.avro.AvroRemoteException {
    	client.addMotionHistoryEffect();
    }

    public void addNegativeEffect() throws org.apache.avro.AvroRemoteException {
    	client.addNegativeEffect();
    }

    public void addSeamCarveEffect() throws org.apache.avro.AvroRemoteException {
    	client.addSeamCarveEffect();
    }

    public void addSepiaEffect() throws org.apache.avro.AvroRemoteException {
    	client.addSepiaEffect();
    }

    public void addVerticalEffect() throws org.apache.avro.AvroRemoteException {
    	client.addVerticalEffect();
    }

    public void addXrayEffect() throws org.apache.avro.AvroRemoteException {
    	client.addXrayEffect();
    }

    public void addFrames(List<ByteBuffer> frames, org.apache.avro.ipc.Callback<List<ByteBuffer>> callback) throws java.io.IOException {
    	client.addFrames(frames, callback);
    }

    public void clearEffects() throws org.apache.avro.AvroRemoteException {
    	client.clearEffects();
    }
}
