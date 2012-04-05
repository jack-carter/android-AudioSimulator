/**
 * 
 */
package com.maniac.tester.audio.values;

import com.maniac.tester.helpers.NamedValue;

import android.media.AudioFormat;

/**
 * @author J Carter
 *
 */
public class Encoding extends NamedValue
{
	static public Encoding[] encodings()
	{
		if ( encodings == null )
		{
			encodings = new Encoding[]
			{
				new Encoding(AudioFormat.ENCODING_PCM_8BIT,"ENCODING_PCM_8BIT"),
				new Encoding(AudioFormat.ENCODING_PCM_16BIT,"ENCODING_PCM_16BIT")
			};
		}
		
		return encodings;
	}
	
	static public Encoding valueOf(int value)
	{
		for (Encoding encoding : encodings())
			if (encoding.value() == value)
				return encoding;
		
		return null;
	}
	
	private Encoding(int encoding,String name)
	{
		super(encoding,name);
	}
	
	static private Encoding[] encodings;
}
