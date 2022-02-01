
package util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JComponent;
import javax.swing.JTextField;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: InputField
 *
 * Date Modified: 2021.08.24
 *
 */
public class InputField
	implements	LineReader,
				ActionListener
{
// =============================================================================
//  INSTANCE VARIABLE:
// =============================================================================
	
	// - PRIVATE VARIABLE ------------------------------------------------------	
	private BlockingQueue<String> queue = null;
	private JTextField txtField = null;
	
// =============================================================================
//  INSTANCE METHOD:
// =============================================================================
	
// -----------------------------------------------------------------------------
//  PUBLIC METHOD:
// -----------------------------------------------------------------------------
	
	// - PUBLIC METHOD ---------------------------------------------------------
	//
	public InputField()
	{
		this.queue = new LinkedBlockingQueue<String>();
		this.txtField = new JTextField();
		this.txtField.addActionListener(this);
	}
	
	// - PUBLIC METHOD ---------------------------------------------------------
	//
	public JComponent getComponent()
	{
		return this.txtField;
	}
	
	// - PUBLIC METHOD ---------------------------------------------------------
	//
	public void putString(String string)
	{
		try {
			this.queue.put(string);
		}
		catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

// -----------------------------------------------------------------------------
//  PUBLIC METHOD: IMPLEMENTS: LineReader
// -----------------------------------------------------------------------------
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: LineReader
	//
	@Override
	public String readLine()
		throws	IOException
	{
		String line = null;
		try {
			line = this.queue.take();
		}
		catch (InterruptedException ex) {
			throw new IOException(ex);
		}
		return line;
	}
	
// -----------------------------------------------------------------------------
//  PUBLIC METHOD: IMPLEMENTS: ActionListener
// -----------------------------------------------------------------------------
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: ActionListener
	//
	@Override
	public void actionPerformed(ActionEvent ev)
	{
		String line = this.txtField.getText();
		try {
			this.txtField.setText("");
			this.queue.put(line);
		}
		catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
