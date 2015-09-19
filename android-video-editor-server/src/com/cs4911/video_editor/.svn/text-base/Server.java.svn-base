package com.cs4911.video_editor;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import com.cs4911.video_editor.effects.IdentityEffect;
import com.cs4911.video_editor.pipeline.EffectTask;
import com.cs4911.video_editor.pipeline.FrameProcessor;
import com.cs4911.video_editor.pipeline.LocalEffectTask;
import com.cs4911.video_editor.testproto.TestProtocol;
import com.cs4911.video_editor.effects.*;

public class Server {
	private static NettyServer server;
	final static int port = 20001;
	
	public class TestProtocolImpl implements TestProtocol {
		FrameProcessor frameProcessor;
		
		void StartFrameProcessor() {
			frameProcessor.start();
		}
		
		public TestProtocolImpl() {
			System.out.println("Starting server side frame processor ");
			frameProcessor = new FrameProcessor(new EffectTask[]{new LocalEffectTask(new IdentityEffect())});
			StartFrameProcessor();
		}
		
		@Override
		public Void addBlurEffect() throws AvroRemoteException {
			System.out.println("Applying Blur Effect");
			frameProcessor.addEffect(new LocalEffectTask(new BlurEffect()));
			StartFrameProcessor();
			return null;
		}

		@Override
		public Void addColorSaturationEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new ColorSaturationEffect()));
			StartFrameProcessor();
			return null;
		}

		@Override
		public Void addDrawingEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new DrawingEffect()));
			return null;
		}

		@Override
		public Void addEdgeDetectionEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new EdgeDetectionEffect()));
			return null;
		}

		@Override
		public Void addGradientMagnitudeEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new GradientMagnitudeEffect()));
			return null;
		}

		@Override
		public Void addGrayscaleEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new GrayscaleEffect()));
			return null;
		}

		@Override
		public Void addHorizontalFlipEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new HorizontalFlipEffect()));
			return null;
		}

		@Override
		public Void addHoughCircleEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new HoughCircleEffect()));
			return null;
		}

		@Override
		public Void addHoughLineEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new HoughLineEffect()));
			return null;
		}

		@Override
		public Void addIdentityEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new IdentityEffect()));
			return null;
		}

		@Override
		public Void addMotionHistoryEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new MotionHistoryEffect()));
			return null;
		}

		@Override
		public Void addNegativeEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new NegativeEffect()));
			return null;
		}

		@Override
		public Void addSeamCarveEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new SeamCarveEffect()));
			return null;
		}

		@Override
		public Void addSepiaEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new SepiaEffect()));
			return null;
		}

		@Override
		public Void addVerticalEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new VerticalFlipEffect()));
			return null;
		}

		@Override
		public Void addXrayEffect() throws AvroRemoteException {
			frameProcessor.addEffect(new LocalEffectTask(new XrayEffect()));
			return null;
		}

		@Override
		public Void clearEffects() throws AvroRemoteException {
			System.out.println("Clearling all effects");
			frameProcessor.clearEffects();
			return null;
		}

		@Override
		public List<ByteBuffer> addFrames(List<ByteBuffer> frames)
				throws AvroRemoteException {
			int numRemaining = 0;
			
			for (ByteBuffer buf: frames) {
				++numRemaining;
				MatOfByte m = new MatOfByte(buf.array());
				Mat frame = Highgui.imdecode(m, 0);
				frameProcessor.addFrame(frame);
			}
			
			List<ByteBuffer> processedFrames = new ArrayList<ByteBuffer>();
			while (numRemaining > 0) {
				Mat newFrame = frameProcessor.getFrame();
				MatOfByte m = new MatOfByte();
				Highgui.imencode(".png", newFrame, m);
				processedFrames.add(ByteBuffer.wrap(m.toArray()));
				--numRemaining;
			}
			
			return processedFrames;
		}
	}
	
	public void InitServer() throws UnknownHostException {
		InetSocketAddress socketAddr = new InetSocketAddress(port);
		while (true) {
			server = new NettyServer(new SpecificResponder(TestProtocol.class,
					new TestProtocolImpl()), socketAddr);
			
			try {
				server.getPort();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			
			break;
		}
		
		System.err.println("Server is ready to accept connections");		
	}
	
	public void StartServer() {
		server.start();
	}
	
	public static void main(String[] args) {
		System.loadLibrary("opencv_java");
		System.out.println("Cloud Server");
		
		Server myServer = new Server();
		try {
			myServer.InitServer();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		myServer.StartServer();
	}
}
