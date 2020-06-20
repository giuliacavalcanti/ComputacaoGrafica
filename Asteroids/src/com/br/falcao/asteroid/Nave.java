package com.br.falcao.asteroid;

import com.jogamp.opengl.GL2;

import com.br.falcao.asteroid.*;

public class Nave implements IRender {
	
	public static float NAVE_Z = 1.5f;
	public static float NAVE_LARGURA = 6.0f;
	public static float NAVE_ALTURA = 6.0f;
	public static float NAVE_Y = 0.5f;
		
	private GL2 gl;

	public Nave(GL2 gl) {
		this.gl = gl;
	}
	
	/**
	 * Renderiza a Nave
	 */
	@Override
	public void render() {
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glPushMatrix();
		gl.glTranslatef(0, NAVE_Y, 0);
		gl.glColor3f(1, 0, 0);

		// desenhar a nave
		float[][] naveVertices2D = { { 0.0f, NAVE_ALTURA/2.0f }, { -NAVE_LARGURA/2.0f, -NAVE_LARGURA/2.0f }, { 0.0f, -(NAVE_ALTURA/2.0f)*0.3f}, { NAVE_LARGURA/2.0f, -NAVE_LARGURA/2.0f } };
		float[] naveZ = { -NAVE_Z/2.0f, NAVE_Z/2.0f };
		float[][] naveCores = {{0.89f,0.83f,0.19f}, {0.62f,0.47f,0.06f}, {0.62f,0.47f,0.06f}, {0.62f,0.47f,0.06f}, {0.62f,0.47f,0.06f}};
		renderizarLados(naveVertices2D, naveZ, naveCores);
		
		float[][] verticesLados = {{ NAVE_LARGURA/2.0f, -NAVE_LARGURA/2.0f }, { 0.0f, -(NAVE_ALTURA/2.0f)*0.3f }, { NAVE_LARGURA/2.0f, -NAVE_LARGURA/2.0f }, { 0.0f, NAVE_ALTURA/2.0f }, { -NAVE_LARGURA/2.0f, -NAVE_LARGURA/2.0f }, { 0.0f, -(NAVE_ALTURA/2.0f)*0.3f }, { -NAVE_LARGURA/2.0f, -NAVE_LARGURA/2.0f }, { 0.0f, NAVE_ALTURA/2.0f }};
		renderizarLados(verticesLados, naveZ);
		 
		gl.glPopMatrix();
		gl.glEnable(GL2.GL_LIGHTING);
	}
	
	private void renderizarLados(float[][] naveVertices2D, float[] naveZ, float[][] naveCores) {
		for (int j = 0; j < naveZ.length; j++) { 
			gl.glBegin(GL2.GL_TRIANGLE_FAN);
						
			for (int i = 0; i < naveVertices2D.length; i++) {
				gl.glColor3f(naveCores[i][0], naveCores[i][1], naveCores[i][2]);
				gl.glVertex3f(naveVertices2D[i][0], naveVertices2D[i][1], naveZ[j]);
			}
			gl.glEnd();
			
		}		
	}
	
	private void renderizarLados(float[][] verticesLados, float[] naveZ) {
		gl.glColor3f(0.41f, 0.48f, 0.8f);
		
		for(int i = 0; i < verticesLados.length; i++) {
			if(i % 2 == 0) {
				gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			}
			
			for(int j = naveZ.length - 1; j >= 0; j--) {
				gl.glVertex3f(verticesLados[i][0], verticesLados[i][1], naveZ[j]);				
			}
			
			if(i % 2 == 1) {
				gl.glEnd();
			}
		}		
	}
	
}
