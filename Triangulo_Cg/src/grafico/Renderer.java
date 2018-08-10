package grafico;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;

public class Renderer {
	private static GLWindow window = null;
	private static EventListener ev = null;
	
	public static void init() {
		//iniciliazar a janela, ou seja, o render
	GLProfile.initSingleton();
		//a versao do jogl que vamos usar
	GLProfile profile = GLProfile.get(GLProfile.GL2);
		//Acessar as capacidades do profile
	GLCapabilities caps = new GLCapabilities(profile);
		//criação da janela
	window = GLWindow.create(caps);
		//dizer o tamanho da janela
	window.setSize(640, 360);
	window.setRealized(false);
	window.setVisible(true);
	window.addGLEventListener(new EventListener());
	
	//faz com que a janela fique statica
	FPSAnimator animator = new FPSAnimator(window, 60);
	animator.start();
	}
}





/* Segundo jeito de inicializar uma janela
 * 
 * GLCanvas canvas = new GLECanvas(caps)
 * Frame frame = new Frame();
 * frame.add(canvas);
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
