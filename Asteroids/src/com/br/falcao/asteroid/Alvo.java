package com.br.falcao.asteroid;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import com.br.falcao.asteroid.*;

public class Alvo implements IRender {
	public static float RAIO_INTERNO = 0.3f;
	public static float RAIO_EXTERNO = 1.0f;
	public static float ALVO_Y = 6.0f;

	private float x;
	private float z;
	private long reduzirTempo;

	private GL2 gl;
	private GLUT glut;

	public Alvo(GL2 gl, float x, float z) {
		this.gl = gl;
		this.glut = new GLUT();

		this.x = x;
		this.z = z;
		this.reduzirTempo = System.currentTimeMillis() + ((int)(5 + Math.random()*10))*1000000;
	}

	/**
	 * Renderiza o alvo
	 */
	@Override
	public void render() {
		if(reduzirTempo >= System.currentTimeMillis()) {
			gl.glPushMatrix();

			gl.glColor3f(0.7f,0.7f,0.0f);
			gl.glTranslatef(x, ALVO_Y, z);
			gl.glRotatef(90,1,0,0);
			glut.glutSolidTorus(RAIO_INTERNO, RAIO_EXTERNO, Jogo.TEXTURA_TAMANHO, Jogo.TEXTURA_TAMANHO);

			gl.glPopMatrix();
		}
	}

	public float getX() {
		return this.x;
	}

	public float getZ() {
		return this.z;
	}

	public boolean estaAtivo() {
		if(reduzirTempo >= System.currentTimeMillis()) {
			return true;
		}

		return false;
	}

}
