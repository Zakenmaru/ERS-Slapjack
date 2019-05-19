import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputter implements KeyListener
{
	public String curLetter;
	private boolean pressed = false;
	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		if (!pressed)
		{
			curLetter = KeyEvent.getKeyText(e.getKeyCode());
		}
		pressed = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		curLetter = null;
		pressed = false;
	}
}