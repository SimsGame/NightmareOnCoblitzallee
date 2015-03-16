/*******************************************************************************
 * Copyright (c) 2013 DHBW.
 * This source is subject to the DHBW Permissive License.
 * Please see the License.txt file for more information.
 * All other rights reserved.
 * 
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY 
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *Project: Zombiz
 *Package: com.dhbw.zombiz
 ********************************************************************************/
package com.dhbw.Zombiz.output.audio;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**the Sound Class contains a sound/musik part
 * in a Byte Array
 * 
 * @author Christoph Schabert
 * @version 1.0
 */




public class Sound implements Runnable {
	
	private int NUM_CHANNELS;
	private int FRAMES;
	private int SAMPLE_RATE;
	private double[] values;
	
	private String path = "src/main/resources/Audio/";
	boolean repeat = false;
	int validBits = 1;
	
	public Sound(String filename){
		readWavFile(path.concat(filename).concat(".wav"));
//		System.out.println(path.concat(filename)+" "+FRAMES+" "+SAMPLE_RATE+" "+values.length);
	}
	

	private void readWavFile(String filePath) {
		try
		{
			// Open the wav file specified as the first argument
			WavFile wavFile = WavFile.openWavFile(new File(filePath));

			// Display information about the wav file
			wavFile.display();

			// Get the number of audio channels in the wav file
			NUM_CHANNELS = wavFile.getNumChannels();
			FRAMES = (int) wavFile.getNumFrames();
			SAMPLE_RATE = (int) wavFile.getSampleRate();
			validBits = wavFile.getValidBits();
			
			// Create a buffer of 100 frames
			int[] buffer = new int[100*NUM_CHANNELS];

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;

			values = new double[(int) (wavFile.getNumFrames() * NUM_CHANNELS)];
			wavFile.readFrames(values, (int)wavFile.getNumFrames());
			
			do
			{
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, 100);

				// Loop through frames and look for minimum and maximum value
				for (int s=0 ; s<framesRead * NUM_CHANNELS ; s++)
				{
					if (buffer[s] > max) max = buffer[s];
					if (buffer[s] < min) min = buffer[s];
					System.out.println();
				} 
			}
			while (framesRead != 0);
			
			// Close the wavFile
			wavFile.close();

			// Output the minimum and maximum value
			System.out.println("Min: "+min);
			System.out.println("Max: "+max);
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}
	
	public void setRepeat(boolean rep){
		repeat = rep;
		return;
	}
	
	public void playSound(){
		
		byte[] sourcearray = new byte[FRAMES];
		
//		int scal = validBits/8;
//		int scal = 1000;
		double max = values[0];
		
		for(int i = 1; i < values.length;i++){
			if(max < values[i])
				max = values[i];
		}
		System.out.println(max);
		
		double scal = 126/max;
		
//		if(validBits != 1){
//			for(int i = 0; i < (validBits/8);i++)
//				scal *= 10;	
//		}
			
		System.out.println(validBits+"/"+scal);
		
		for(int i = 0; i < FRAMES; i++){
			sourcearray[i] = ((byte) (values[i]*scal));
//			System.out.println(sourcearray[i]+"/"+values[i]);
		}
		
		AudioFormat af = new AudioFormat(SAMPLE_RATE,8,NUM_CHANNELS,true,false);
       
		try {
			sdl = AudioSystem.getSourceDataLine(af);
		    sdl.open(af);
		    sdl.start();
		    System.out.println("start rep");
		    sdl.write(sourcearray,0,sourcearray.length);
		    System.out.println("reeeeeeeeeeep");
		    sdl.drain();
		    sdl.close();
			
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return;
	}
	
	private  SourceDataLine sdl;


	public void run() {
		do{
			

		playSound();
	    }while(repeat);
	}
	
	public void stop(){
		System.out.println("Stoped !");
		repeat = false;

			sdl.stop();
		
		
	}
	
	
	
}