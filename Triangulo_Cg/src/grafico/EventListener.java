package grafico;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

public class EventListener implements GLEventListener {

	@Override
	public void display(GLAutoDrawable drawble) {
		// Chamado SEMPRE para desenhar sempre
		GL2 gl = drawble.getGL().getGL2();
		
		//cor da tela
		//gl.glClearColor(1, 0, 0, 1);
		//o que queremos limpar da tela
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		//rgb sem alfa, que é o terceiro parametro
		gl.glColor3f(1, 0, 0);
		gl.glBegin(GL2.GL_TRIANGLES);
		gl.glVertex2f(-0.5f,0.1f); 
		gl.glVertex2f(0.0f,.9f); 
		gl.glVertex2f(0.5f,0.0f); 
		//gl.glVertex2f(-0.5f, -0.5f);
		
		//
		
		gl.glEnd();
	}

	@Override
	public void dispose(GLAutoDrawable drawble) {
		// chamado quando o GL acaba
		
	}

	@Override
	public void init(GLAutoDrawable drawble) {
		// chamado quando GL é configurado
		GL2 gl = drawble.getGL().getGL2();
		//inicializar a tela
		gl.glClearColor(0, 0, 0, 1);
		
		
	}

	@Override
	public void reshape(GLAutoDrawable drawble, int arg1, int arg2, int arg3, int arg4) {
		// Chamado para reajustar a janela
		
	}

}
