
package desktop;

import static desktop.DesktopMessage.CONTROL_ACCEPTED;
import static desktop.DesktopMessage.CONTROL_MESSAGE;
import static desktop.DesktopMessage.CONTROL_REFUSED;
import static desktop.DesktopMessage.START_CONTROL;
import static desktop.DesktopMessage.STOP_CONTROL;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.UUID;

import javax.swing.JFrame;

import com.midfield_system.api.stream.StreamException;
import com.midfield_system.api.system.CommPacket;
import com.midfield_system.api.system.CommPacketHandler;
import com.midfield_system.api.system.ObjectId;
import com.midfield_system.api.system.PacketCommunicator;
import com.midfield_system.api.system.PacketIoException;
import com.midfield_system.api.system.SystemException;
import com.midfield_system.api.util.Log;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: DesktopServer 
 *
 * Date Modified: 2021.10.27
 *
 */

//==============================================================================
public class DesktopServer
	implements	CommPacketHandler
{
	//- PUBLIC CONSTANT VALUE --------------------------------------------------
	public static final String DESKTOP_SERVER = "DesktopServer";
	
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String
		STR_ACCEPT_CONTROL	= "�����u(%s)����̃f�X�N�g�b�v������J�n���܂��D",
		STR_REJECT_CONTROL	= "�����u(%s)����̃f�X�N�g�b�v��������ۂ��܂��D",
		STR_STOP_CONTROL	= "�����u����̃f�X�N�g�b�v������I�����܂��D";
	
	private static final String
		STR_CANT_CREATE_ROBOT	= "�f�X�N�g�b�v����p�̃C���X�^���X�𐶐��ł��܂���D",
		STR_UNREACHABLE			= "�N���C�A���g�֑��M�����p�P�b�g�����B�ł��܂���ł���",
		STR_ILLEGAL_MESSAGE		= "�s���ȃ��b�Z�[�W����M���܂����D: %s from %s\n";	

	private static enum ControlState
	{
		WAIT,		// �f�X�N�g�b�v�������ҋ@��
		CONTROLLED	// �f�X�N�g�b�v��������
	}

//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================
	
	//- PRIVATE VARIABLE -------------------------------------------------------
	private final PacketCommunicator comm;
	
	private final double scaleX;
	private final double scaleY;
	private final Robot robot;
	
	private ImageSender imgSender = null;
	private ControlState state = ControlState.WAIT;
	
//==============================================================================
//  INSTANCE METHOD:
//==============================================================================

//------------------------------------------------------------------------------
//  PUBLIC METHOD:
//------------------------------------------------------------------------------
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPREMENTS: CommPacketHandler
	//
	@Override
	public synchronized boolean handleIncomingPacket(CommPacket pkt)
	{
		boolean wasHandled = true;
		
		// �p�P�b�g���� DesktopMessage �̃C���X�^���X���擾����D
		DesktopMessage msg = pkt.getSerializableObject(DesktopMessage.class);
		if (msg == null) {
			wasHandled = false;
			return wasHandled;
		}
		// ���M���� ObjectId ���擾����D
		ObjectId srcId = pkt.getSourceObjectId();
		
		// ���b�Z�[�W�^�C�v���̏��������s����D
		String type = pkt.getMessageType();
		switch (type) {
		case START_CONTROL	: msgHn_StartControl(msg, srcId);		break;
		case CONTROL_MESSAGE: msgHn_ControlMessage(msg);			break;
		case STOP_CONTROL	: msgHn_StopControl(msg);				break;
		default 			: msgHn_UnsupportedMessage(type, srcId);break;
		}
		return wasHandled;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPREMENTS: CommPacketHandler
	//
	@Override
	public void handlePacketIoException(PacketIoException ex)
	{
		Log.warning(ex);
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPREMENTS: CommPacketHandler
	//
	@Override
	public void handleUnreachablePacket(CommPacket inPkt)
	{
		Log.warning(STR_UNREACHABLE);
	}
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- CONSTRUCTOR ------------------------------------------------------------
	//
	DesktopServer(JFrame frame)
		throws	SystemException
	{
		// PacketCommunicator �𐶐�����D
		this.comm = new PacketCommunicator(DESKTOP_SERVER, this);
			// SystemException
		
		GraphicsConfiguration config = frame.getGraphicsConfiguration();
		AffineTransform transform = config.getDefaultTransform();
		this.scaleX = transform.getScaleX();
		this.scaleY = transform.getScaleY();
		
		// �f�X�N�g�b�v����p�� Robot �𐶐�����D
		try {
			this.robot = new Robot();
		}
		catch (AWTException ex) {
			SystemException sysEx
				= new SystemException(STR_CANT_CREATE_ROBOT, ex);
			throw sysEx;
		}
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	boolean isControlled()
	{
		return (this.state == ControlState.CONTROLLED);
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void close()
	{
		stopControl();
		this.comm.close();
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	synchronized void stopControl()
	{
		if (this.state == ControlState.CONTROLLED) {
			// ��ԕύX�F�f�X�N�g�b�v�������ҋ@���D
			this.state = ControlState.WAIT;
			
			// DesktopImageSender ���I������D
			if (this.imgSender != null) {
				this.imgSender.close();
				this.imgSender = null;
			}
			Log.message(STR_STOP_CONTROL);
		}
	}	
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD: START_CONTROL
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void msgHn_StartControl(DesktopMessage inMsg, ObjectId srcId)
	{
		String msgType = CONTROL_REFUSED;
		Serializable obj = null;
		
		// �\�[�X�z�X�g�A�h���X���擾����D
		UUID nodeId = srcId.getNodeId();
		String srcNodeId = nodeId.toString();
		
		// �f�X�N�g�b�v����̎���ۂ��m�F����D
		boolean doAccept = checkAccept(inMsg);
		if (doAccept) {
			// �f�X�N�g�b�v������󂯓����D
			try {
				// DesktopImageSender �𐶐�����D
				this.imgSender = new ImageSender(this);
					// SystemException, StreamException
				
				// ������b�Z�[�W���o�͂���D
				Log.message(STR_ACCEPT_CONTROL, srcNodeId);
				
				// ���b�Z�[�W�^�C�v�̐ݒ�Əo�̓p�����[�^�̎擾�D
				msgType = CONTROL_ACCEPTED;
				obj = this.imgSender.getOutputParam();
			}
			catch (SystemException | StreamException ex) {
				// DesktopImageSender �̐����Ɏ��s�����̂ŁC
				// �f�X�N�g�b�v��������ۂ���D
				Log.message(STR_REJECT_CONTROL, srcNodeId);
				Log.message(ex);
				msgType = CONTROL_REFUSED;
				obj = ex;
			}
		}
		else {
			// �f�X�N�g�b�v��������ۂ���D
			Log.message(STR_REJECT_CONTROL, srcNodeId);
			msgType = CONTROL_REFUSED;
			obj = null;
		}
		// �f�X�N�g�b�v�������v���ɑ΂��鉞����Ԃ��D
		ObjectId dstId = srcId;
		CommPacket pkt = new CommPacket(msgType, dstId);
		DesktopMessage msg = new DesktopMessage(obj);
		pkt.setSerializableObject(msg);
		this.comm.dispatchPacket(pkt);
		
		// �f�X�N�g�b�v������󂯓����ꍇ�́C
		// ��ԕύX�F�f�X�N�g�b�v���������D
		if (msgType.equals(CONTROL_ACCEPTED)) {
			this.state = ControlState.CONTROLLED;
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private boolean checkAccept(DesktopMessage msg)
	{
		// �f�X�N�g�b�v�������ҋ@���̏ꍇ�́C���u������󂯓����D
		// �f�X�N�g�b�v���������̏ꍇ�́C������󂯓���Ȃ��D
		return (this.state == ControlState.WAIT);
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD: STOP_CONTROL
//------------------------------------------------------------------------------

	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void msgHn_StopControl(DesktopMessage msg)
	{
		stopControl();
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD: CONTROL_MESSAGE
//------------------------------------------------------------------------------

	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void msgHn_ControlMessage(DesktopMessage msg)
	{
		// �f�X�N�g�b�v�������ҋ@���̏ꍇ�͖߂�D
		if (this.state == ControlState.WAIT) {
			return;
		}
		// Action ���̏��������s����D
		DesktopMessage.Action action = msg.getAction();
		Object obj = msg.getObject();
		
		switch (action) {
		case KEY_PRESS		: evHn_KeyPress((KeyEvent)obj);				break;
		case KEY_RELEASE	: evHn_KeyRelease((KeyEvent)obj);			break;
		case MOUSE_PRESS	: evHn_MousePress((MouseEvent)obj);			break;
		case MOUSE_RELEASE	: evHn_MouseRelease((MouseEvent)obj);		break;
		case MOUSE_MOVE		: evHn_MouseMove((MouseEvent)obj);			break;
		case MOUSE_WHEEL	: evHn_MouseWheel((MouseWheelEvent)obj);	break;
		case UNKNOWN		: /* error */								break;
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_KeyPress(KeyEvent ev)
	{
		int keyCode = ev.getKeyCode();
		this.robot.keyPress(keyCode);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_KeyRelease(KeyEvent ev)
	{
		int keyCode = ev.getKeyCode();
		this.robot.keyRelease(keyCode);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_MousePress(MouseEvent ev)
	{
		int btnNum = getMouseButtonNumber(ev);
		if (btnNum != 0) {
			this.robot.mousePress(btnNum);
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_MouseRelease(MouseEvent ev)
	{
		int btnNum = getMouseButtonNumber(ev);
		if (btnNum != 0) {
			this.robot.mouseRelease(btnNum);
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_MouseMove(MouseEvent ev)
	{
		int x = (int)(ev.getX() / this.scaleX);
		int y = (int)(ev.getY() / this.scaleY);
		this.robot.mouseMove(x, y);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_MouseWheel(MouseWheelEvent ev)
	{
		int wheelAmt = ev.getWheelRotation();
		this.robot.mouseWheel(wheelAmt);
	}
	
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private int getMouseButtonNumber(MouseEvent ev)
	{
		int btnNum = 0;
		
		switch (ev.getButton()) {
		case MouseEvent.NOBUTTON: btnNum = 0;								break;
		case MouseEvent.BUTTON1	: btnNum = InputEvent.BUTTON1_DOWN_MASK;	break;
		case MouseEvent.BUTTON2	: btnNum = InputEvent.BUTTON2_DOWN_MASK;	break;
		case MouseEvent.BUTTON3	: btnNum = InputEvent.BUTTON3_DOWN_MASK;	break;
		default 				: /* error */								break;
		}
		return btnNum;
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD: Illigal message
//------------------------------------------------------------------------------

	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void msgHn_UnsupportedMessage(String type, ObjectId srcId)
	{
		Log.warning(STR_ILLEGAL_MESSAGE, type, srcId);
	}
}
