package com.br.falcao.asteroid;

import java.io.IOException;
import java.io.InputStream;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import com.br.falcao.asteroid.*;

public class Base implements IRender {

	public static final float TAMANHO_BASE = 25.0f;
	public static final float[] BASE_COR = {0.6f, 0.6f, 0.6f};
	public static final float BASE_Y = -4.0f;
	
	private Texture texturaDaBase;
	
	float[][] vertices; 
	float[][] coordenadasParaTexto;
	float[][] normalVectors;
	
	private GL2 gl;
	
	public Base(GL2 gl) {
		this.gl = gl;
		carregarTexturas();
		geradorDeCoordenadas();
	}
	
    /**
     * Loads textures
     */
    private void carregarTexturas() {
        try {
            InputStream stream = getClass().getResourceAsStream("images/floor.jpg");
            texturaDaBase = TextureIO.newTexture(stream, false, "jpg");
        } catch (IOException e) {
            System.out.println("Floor textures failed.");
        }        
    }		
    
    private void geradorDeCoordenadas() {
		float coordenadaInicialBase = (TAMANHO_BASE / 2.0f);
		int totalRetangulosChao = Jogo.TEXTURA_TAMANHO;
		float increment = (TAMANHO_BASE / totalRetangulosChao);
		
		vertices = new float[totalRetangulosChao*totalRetangulosChao*4][3];
		coordenadasParaTexto = new float[totalRetangulosChao*totalRetangulosChao*4][2];		
    	normalVectors = new float[totalRetangulosChao*totalRetangulosChao*4][3];
		
    	int count = 0;
		for (int v = 0; v < totalRetangulosChao; v++) {
			for (int h = 0; h < totalRetangulosChao; h++) {			
				
				vertices[count][0] = coordenadaInicialBase - h*increment; vertices[count][1] = BASE_Y; vertices[count][2] = coordenadaInicialBase - (v+1)*increment;
				vertices[count+1][0] = coordenadaInicialBase - h*increment; vertices[count+1][1] = BASE_Y; vertices[count+1][2] = coordenadaInicialBase - v*increment;
				vertices[count+2][0] = coordenadaInicialBase - (h+1)*increment; vertices[count+2][1] = BASE_Y; vertices[count+2][2] = coordenadaInicialBase - (v+1)*increment;
				vertices[count+3][0] = coordenadaInicialBase - (h+1)*increment; vertices[count+3][1] = BASE_Y; vertices[count+3][2] = coordenadaInicialBase - v*increment; 
			
				coordenadasParaTexto[count][0] = h*(1.0f/totalRetangulosChao); coordenadasParaTexto[count][1] = (v+1)*(1.0f/totalRetangulosChao);
				coordenadasParaTexto[count+1][0] = h*(1.0f/totalRetangulosChao); coordenadasParaTexto[count+1][1] = v*(1.0f/totalRetangulosChao);
				coordenadasParaTexto[count+2][0] = (h+1)*(1.0f/totalRetangulosChao); coordenadasParaTexto[count+2][1] = (v+1)*(1.0f/totalRetangulosChao);
				coordenadasParaTexto[count+3][0] = (h+1)*(1.0f/totalRetangulosChao); coordenadasParaTexto[count+3][1] = (v)*(1.0f/totalRetangulosChao);
				
				normalVectors[count] = produtoVetor(subtracaoVetor(vertices[2], vertices[0]), subtracaoVetor(vertices[1], vertices[0]));
     			normalVectors[count+1] = produtoVetor(subtracaoVetor(vertices[0], vertices[1]), subtracaoVetor(vertices[3], vertices[1]));
				normalVectors[count+2] = produtoVetor(subtracaoVetor(vertices[3], vertices[2]), subtracaoVetor(vertices[0], vertices[2]));
				normalVectors[count+3] = produtoVetor(subtracaoVetor(vertices[1], vertices[3]), subtracaoVetor(vertices[2], vertices[3]));						

				count = count + 4;
			}
		}    	
    }
    
    @Override
	public void render() {
		gl.glPushMatrix();
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		texturaDaBase.enable(gl);
		texturaDaBase.bind(gl);
		
		gl.glColor3f(BASE_COR[0], BASE_COR[1], BASE_COR[2]);

		for (int i = 0; i < vertices.length; i++) {
				if(i % 4 == 0) { gl.glBegin(GL2.GL_TRIANGLE_STRIP); }
				
					gl.glNormal3f(normalVectors[i][0], normalVectors[i][1], normalVectors[i][2]);
					gl.glTexCoord2f(coordenadasParaTexto[i][0], coordenadasParaTexto[i][1]);		
					gl.glVertex3f(vertices[i][0], vertices[i][1], vertices[i][2]);

				if(i % 4 == 3) { gl.glEnd(); }
		}
		
		gl.glDisable(GL.GL_TEXTURE_2D);		
		gl.glPopMatrix();
	}
    
    public static float[] produtoVetor(float[] a, float[] b) {
		float[] result = {a[1]*b[2] - a[2]*b[1], a[2]*b[0] - a[0]*b[2], a[0]*b[1] - a[1]*b[0]};
		return result;
	}
    
    public static float[] subtracaoVetor(float[] a, float[] b) {
		float[] result = {a[0] - b[0], a[1] - b[1], a[2] - b[2]};
		return result;
	}

}
