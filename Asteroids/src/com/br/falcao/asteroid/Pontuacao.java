package com.br.falcao.asteroid;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Pontuacao implements IRender {

	private long endTime;
	
	private int janelaLargura;
	private int janelaAltura;

	private int alvosQueApareceram;
	private int alvosDestruidos;
	private int tirosPerdidos;	
	
	private GL2 gl;
	private GLUT glut;
	
	public Pontuacao(GL2 gl, long endTime, int janelaLargura, int janelaAltura) {
		this.gl = gl;
		this.glut = new GLUT();
		
		this.endTime = endTime;
		this.janelaAltura = janelaAltura;
		this.janelaLargura = janelaLargura;
		
		alvosQueApareceram = alvosDestruidos = tirosPerdidos = 0;
	}
	
	/**
	 * Renderiza pontuacao
	 */
	@Override
	public void render() {
		if(System.currentTimeMillis() <= endTime) {
			renderScore();
		} else {
			renderFinalScore();
		}
	}
	
	/**
	 * Renders intermediate score
	 */
	private void renderScore() {		
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		String time = "Time: " + (endTime - System.currentTimeMillis())/1000;
		String contAlvosMostrados = "Alvos que apareceram: " + alvosQueApareceram;
		String contAlvosDestruidos = "Alvos destruidos: " + alvosDestruidos;
		String contTiros = "Misseis destruidos: " + tirosPerdidos;
		
		GLUT glut = new GLUT();		
		
		gl.glWindowPos2f(janelaLargura/2.0f - 9*time.length()/2, janelaAltura - 9 - 10);
		glut.glutBitmapString(GLUT.BITMAP_9_BY_15, time);			
		
		gl.glWindowPos2f(10, 10); 		
		glut.glutBitmapString(GLUT.BITMAP_9_BY_15, contAlvosMostrados);	
		
		gl.glWindowPos2f(janelaLargura/2.0f - 9*contAlvosDestruidos.length()/2, 10);		
		glut.glutBitmapString(GLUT.BITMAP_9_BY_15, contAlvosDestruidos);
		
		gl.glWindowPos2f(janelaLargura - 9*contTiros.length() - 10, 10);		
		glut.glutBitmapString(GLUT.BITMAP_9_BY_15, contTiros);			
	}
	
	/**
	 * Fim do jogo
	 */
	private void renderFinalScore() {
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		String fimDeJogo = "Fim de jogo!";
		String totalAlvosMostrados = "Quantidade de alvos que apareceram: " + alvosQueApareceram;
		String totalAlvosDestruidos = "Quantidade de alvos destruidos: " + alvosDestruidos;
		String totalTirosAtirados = "Quantidade de tiros perdidos: " + tirosPerdidos;
		
		gl.glWindowPos2f(janelaLargura/2.0f - 9*fimDeJogo.length()/2, (janelaAltura / 2.0f) + 15 + 10 + 15 + 10); 		
		glut.glutBitmapString(GLUT.BITMAP_9_BY_15, fimDeJogo);			
		
		gl.glWindowPos2f(janelaLargura/2.0f - 9*totalAlvosMostrados.length()/2, (janelaAltura / 2.0f) + 15 + 10); 		
		glut.glutBitmapString(GLUT.BITMAP_9_BY_15, totalAlvosMostrados);	
		
		gl.glWindowPos2f(janelaLargura/2.0f - 9*totalAlvosDestruidos.length()/2, janelaAltura / 2.0f);		
		glut.glutBitmapString(GLUT.BITMAP_9_BY_15, totalAlvosDestruidos);
		
		gl.glWindowPos2f(janelaLargura/2.0f - 9*totalTirosAtirados.length()/2, (janelaAltura / 2.0f) - 15 - 10);		
		glut.glutBitmapString(GLUT.BITMAP_9_BY_15, totalTirosAtirados);			
	}
	
	public void adicionaNovosAlvos(int n) {
		this.alvosQueApareceram += n;
	}
	
	public void adicionaNovosAlvos() {
		this.alvosQueApareceram++;
	}
	
	public void contaAlvosDestruidos() {
		this.alvosDestruidos++;
	}

	public void contaTirosPerdidos() {
		this.tirosPerdidos++;
	}
	
	public int getAlvosQueApareceram() {
		return alvosQueApareceram;
	}

	public int getAlvosDestruidos() {
		return alvosDestruidos;
	}

	public int getTirosPerdidos() {
		return tirosPerdidos;
	}

	public void setJanelaLargura(int largura) {
		this.janelaLargura = largura;
	}

	public void setJanelaAltura(int altura) {
		this.janelaAltura = altura;
	}	


}
