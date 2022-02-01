
package remocon;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.midfield_system.gui.misc.PopupMessage;

/*----------------------------------------------------------------------------*/
/**
 * ServerAddressPanel
 *
 * Copyright (C) Koji Hashimoto
 *
 * Date Modified: 2021.08.26
 * Koji Hashimoto 
 *
 */
@SuppressWarnings("serial")
class ServerAddressPanel
	extends		JPanel
	implements	ActionListener,
				KeyListener,
				InputMethodListener
{
	// - PACKAGE CONSTANT VALUE ------------------------------------------------
	static enum ConnectionState
	{
		IDLE,		// �A�C�h����� 
		CONNECTING,	// �R�l�N�V�����m���������
		CONTROLLING	// ������
	}	

	// - PRIVATE CONSTANT VALUE ------------------------------------------------
	private static final String
		STR_START_CONTROL	= Messages.getString("ServerAddressPanel.0"), //$NON-NLS-1$
		STR_STOP_CONTROL	= Messages.getString("ServerAddressPanel.1"); //$NON-NLS-1$
	
	private static final String
		STR_PLEASE_INPUT	= Messages.getString("ServerAddressPanel.2"), //$NON-NLS-1$
		STR_INVALID_ADDR	= Messages.getString("ServerAddressPanel.3"); //$NON-NLS-1$

	private static final String
		STR_REMOTE_HOST		= Messages.getString("ServerAddressPanel.4"); //$NON-NLS-1$
	
// =============================================================================
//  INSTANCE VARIABLE:
// =============================================================================

	// - PRIVATE VARIABLE ------------------------------------------------------
	private RemoteController remocon = null;
	
	private JTextField addrFld = null;
	private JButton ctlBtn = null;

	private ConnectionState stat = ConnectionState.IDLE;
	private boolean isEditing = false;	
	
// =============================================================================
//  INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
//  PUBLIC METHOD: IMPLEMENTS: ActionListener
// -----------------------------------------------------------------------------

	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: ActionListener
	//
	@Override
	public void actionPerformed(ActionEvent ev)
	{
		String appCmd = ev.getActionCommand();
		if (appCmd.equals(STR_START_CONTROL)) {
			evHn_StartControl();
		}
		else if (appCmd.equals(STR_STOP_CONTROL)) {
			evHn_StopControl();
		}	
	}	

// -----------------------------------------------------------------------------
//  PUBLIC METHOD: IMPLEMENTS: KeyListener
// -----------------------------------------------------------------------------

	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: KeyListener
	//
	@Override
	public void keyTyped(KeyEvent ev)
	{
		// Not Implemented.
	}

	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: KeyListener
	//
	@Override
	public void keyPressed(KeyEvent ev)
	{
		// Not Implemented.
	}

	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: KeyListener
	//
	@Override
	public void keyReleased(KeyEvent ev)
	{
		if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
			if (this.isEditing) {
				this.isEditing = false;
			}
			else {
				this.ctlBtn.doClick();
			}
		}
	}
	
// -----------------------------------------------------------------------------
//  PUBLIC METHOD: IMPLEMENTS: InputMethodListener
// -----------------------------------------------------------------------------

	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: InputMethodListener
	//
	@Override
	public void caretPositionChanged(InputMethodEvent ev)
	{
		// Not Implemented.
	}

	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: InputMethodListener
	//
	@Override
	public void inputMethodTextChanged(InputMethodEvent ev)
	{
		this.isEditing = true;
	}
	
// -----------------------------------------------------------------------------
//  PACKAGE METHOD:
// -----------------------------------------------------------------------------

	// - PACKAGE METHOD --------------------------------------------------------
	//	
	ServerAddressPanel(RemoteController remocon)
	{
		this.remocon = remocon;
		setupGui();	
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	boolean isControllingState()
	{
		boolean isControlling = false;
		if (this.stat == ConnectionState.CONTROLLING) {
			isControlling = true;
		}
		return isControlling;
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	void setState(ConnectionState state)
	{
		switch (state) {
		case IDLE:
			this.addrFld.setEnabled(true);		
			this.ctlBtn.setEnabled(true);
			this.ctlBtn.setText(STR_START_CONTROL);		
			this.stat = ConnectionState.IDLE;
			break;
			
		case CONNECTING:
			this.addrFld.setEnabled(false);
			this.ctlBtn.setEnabled(false);		
			this.stat = ConnectionState.CONNECTING;
			break;
			
		case CONTROLLING: 
			this.addrFld.setEnabled(false);
			this.ctlBtn.setEnabled(true);
			this.ctlBtn.setText(STR_STOP_CONTROL);
			this.stat = ConnectionState.CONTROLLING;
			break;
		}
	}
	
// -----------------------------------------------------------------------------
//  PRIVATE METHOD:
// -----------------------------------------------------------------------------	

	// - PRIVATE METHOD --------------------------------------------------------
	//
	private void setupGui()
	{
		this.addrFld = new JTextField();
		this.addrFld.setEditable(true);
		this.addrFld.addKeyListener(this);
		this.addrFld.addInputMethodListener(this);
		
		this.ctlBtn = new JButton(STR_START_CONTROL);
		this.ctlBtn.addActionListener(this);
		
		setLayout(new BorderLayout());
		add(new JLabel(STR_REMOTE_HOST), BorderLayout.WEST);
		add(this.addrFld, BorderLayout.CENTER);
		add(this.ctlBtn, BorderLayout.EAST);
	}
	
// -----------------------------------------------------------------------------
//  PRIVATE METHOD: actionPerformed()
// -----------------------------------------------------------------------------
	
	// - PRIVATE METHOD --------------------------------------------------------
	//	
	void evHn_StartControl()
	{
		// �ڑ���A�h���X���擾����D
		String address = getSelectedAddress();
		if (address == null) {
			return;
		}
		// �R�l�N�V�����A�h���X�p�l���̏�Ԃ�ύX����D
		setState(ConnectionState.CONNECTING);

		// RPC�N���C�A���g�̏������J�n����D
		this.remocon.startRemoteControl(address);
	}
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private String getSelectedAddress()
	{
		String dstAddr = this.addrFld.getText();
		if ((dstAddr == null) || (dstAddr.length() == 0)) {
			// �A�h���X�����͂���Ă��Ȃ��D
			PopupMessage.warning(this.remocon, STR_PLEASE_INPUT);
			dstAddr = null;
			return dstAddr;
		}
		// ���͂��ꂽ�A�h���X���m�F����D
		try {
			InetAddress.getByName(dstAddr);
							// UnknownHostException
			return dstAddr;
		}
		catch (UnknownHostException ex) {
			// ���͂��ꂽ�A�h���X���s���D
			PopupMessage.warning(
				this.remocon, String.format(STR_INVALID_ADDR, dstAddr)
			);
			dstAddr = null;
			return dstAddr;
		}
	}

	// - PRIVATE METHOD --------------------------------------------------------
	//	
	void evHn_StopControl()
	{
		// RPC�N���C�A���g�̏������~����D
		this.remocon.stopRemoteControl();
	}	
}
