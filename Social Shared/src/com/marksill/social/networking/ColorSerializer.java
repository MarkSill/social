package com.marksill.social.networking;

import org.newdawn.slick.Color;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class ColorSerializer extends Serializer<Color> {

	@Override
	public Color read(Kryo kryo, Input input, Class<Color> type) {
		return new Color(input.readInt(), input.readInt(), input.readInt(), input.readInt());
	}

	@Override
	public void write(Kryo kryo, Output output, Color object) {
		output.writeInt(object.getRed());
		output.writeInt(object.getGreen());
		output.writeInt(object.getBlue());
		output.writeInt(object.getAlpha());
	}

}
