package com.br.falcao.asteroid;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import com.br.falcao.asteroid.*;

public class Tiro implements IRender {
	public static float TIRO_RAIO = 0.5f;
	public static float TIRO_COMECO_Y = Nave.NAVE_Y + Nave.NAVE_ALTURA/2.0f;
	public static float TIRO_CORTE = TIRO_COMECO_Y + 6.0f;
	
	float x;
	float y;
	float z;
	private boolean ativo;
	
	private GL2 gl;
	private GLUT glut;
	
	public Tiro(GL2 gl, float x, float z) {
		this.gl = gl;
		this.glut = new GLUT();
		
		this.x = x;
		this.z = z;
		this.y = TIRO_COMECO_Y;
		
		this.ativo = true;
	}
	
	/**
	 * Renderiza o tiro
	 */
	@Override
	public void render() {
		if(y >= TIRO_CORTE && ativo) {
			ativo = false;
		}
		
		if(ativo) {
			gl.glPushMatrix();
	
			gl.glColor3f(0,0,1);
			gl.glTranslatef(x, y, z);
			gl.glRotatef(90,1,0,0);
			glut.glutSolidSphere(TIRO_RAIO, Jogo.TEXTURA_TAMANHO, Jogo.TEXTURA_TAMANHO);
			
			gl.glPopMatrix();
			
			y += 0.1f;
		}
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getZ() {
		return this.z;
	}
	
	public float getY() {
		return this.y;
	}
	
	public boolean estaAtivo() {
		return this.ativo;
	}
	
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

}
