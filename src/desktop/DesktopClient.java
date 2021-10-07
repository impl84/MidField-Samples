
package desktop;

import java.io.Serializable;
import java.util.UUID;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.IoParam;
import com.midfield_system.api.system.CommPacket;
import com.midfield_system.api.system.CommPacketHandler;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.system.ObjectId;
import com.midfield_system.api.system.PacketCommunicator;
import com.midfield_system.api.system.PacketIoException;
import com.midfield_system.api.system.SystemException;

import desktop.DesktopMessage.Subtype;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: DesktopClient 
 *
 * Date Modified: 2021.09.20
 *
 */

//==============================================================================
public class DesktopClient
	implements	CommPacketHandler
{
	//- PUBLIC CONSTANT VALUE --------------------------------------------------	
	public static final String
		DESKTOP_CLIENT = "DesktopClient"; //$NON-NLS-1$
	
	//- PRIVATE CONSTANT VALUE -------------------------------------------------	
	private static final String
		STR_UNREACHABLE		= "�T�[�o�֑��M�����p�P�b�g�����B�ł��܂���ł���",
		STR_CONTROL_REFUSED	= "���u����v�������ۂ���܂����D",
		STR_ILLEGAL_MESSAGE	= "�s���ȃ��b�Z�[�W����M���܂����D: %s from %s\n";
	
//==============================================================================
//  CLASS VARIABLE:
//==============================================================================
	
	//- PRIVATE STATIC VARIABLE ------------------------------------------------
	private static int instanceNumber = 0;
	
//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================
	
	//- PRIVATE VARIABLE -------------------------------------------------------
	private final DesktopViewer viewer;
	
	private final PacketCommunicator comm;
	private final ObjectId svrId;
	
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
	public boolean handleIncomingPacket(CommPacket pkt)
	{
		boolean wasHandled = true;
		
		// ���̓p�P�b�g�� DesktopMessage �p�̃p�P�b�g�ł��邱�Ƃ��m�F����D
		String msgType = pkt.getMessageType();
		if (msgType.equals(DesktopMessage.MESSAGE_TYPE) == false) {
			wasHandled = false;
			return wasHandled;
		}
		// �p�P�b�g���� DesktopMessage �̃C���X�^���X���擾����D
		DesktopMessage msg = pkt.getSerializableObject(DesktopMessage.class);
		if (msg == null) {
			wasHandled = false;
			return wasHandled;
		}
		// ���M���� ObjectId ���擾����D
		ObjectId srcId = pkt.getSourceObjectId();
		
		// ���b�Z�[�W�̃T�u�^�C�v���̏��������s����D
		switch (msg.getSubtype()) {
		case CONTROL_ACCEPTED	: msgHn_ControlAccepted(msg);		break;
		case CONTROL_REFUSED	: msgHn_ControlRefused(msg);		break;
		default 				: msgHn_illegalMessage(msg, srcId);	break;
		}
		return wasHandled;
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPREMENTS: CommPacketHandler
	//
	@Override
	public void handlePacketIoException(PacketIoException ex)
	{
		// �p�P�b�g���o�͂Ɋւ����O���������s����D
		SwingUtilities.invokeLater(
			() -> this.viewer.handleIllegalMessage(ex)
		);
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPREMENTS: CommPacketHandler
	//
	@Override
	public void handleUnreachablePacket(CommPacket inPkt)
	{
		// PacketIoException �𐶐����C
		// �p�P�b�g���o�͂Ɋւ����O���������s����D
		PacketIoException ex = new PacketIoException(STR_UNREACHABLE, inPkt);
		SwingUtilities.invokeLater(
			() -> this.viewer.handleIllegalMessage(ex)
		);
	}
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- CONSTRUCTOR ------------------------------------------------------------
	//
	DesktopClient(DesktopViewer viewer, String svrAddr)
		throws	SystemException
	{
		// DesktopViewer �̎Q�Ƃ�ێ�����D
		this.viewer = viewer;
		
		// PacketCommunicator �𐶐�����D
		String name = new String(DESKTOP_CLIENT + DesktopClient.instanceNumber++);
		this.comm = new PacketCommunicator(name, this);
			// SystemException
		
		// ���b�Z�[�W�z����� ObjectId �𐶐�����D
		MfsNode mfs = MfsNode.getInstance();
		UUID svrNodeId = mfs.resolveNodeId(svrAddr);
			// SystemException
		
		this.svrId = new ObjectId(DesktopServer.DESKTOP_SERVER, svrNodeId);
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void startControl(String dstAddr)
	{
		// ���u����J�n���b�Z�[�W��z������D
		DesktopMessage msg = new DesktopMessage(Subtype.START_CONTROL, null);
		CommPacket pkt = new CommPacket(DesktopMessage.MESSAGE_TYPE, this.svrId);
		pkt.setSerializableObject(msg);
		this.comm.dispatchPacket(pkt);
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void dispatchEvent(DesktopMessage.Action action, Serializable obj)
	{
		// ���u���상�b�Z�[�W��z������D
		DesktopMessage msg = new DesktopMessage(Subtype.CONTROL_MESSAGE, action, obj);
		CommPacket pkt = new CommPacket(DesktopMessage.MESSAGE_TYPE, this.svrId);
		pkt.setSerializableObject(msg);
		this.comm.dispatchPacket(pkt);
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void stopControl()
	{
		// ���u�����~���b�Z�[�W��z������D
		DesktopMessage msg = new DesktopMessage(Subtype.STOP_CONTROL, null);
		CommPacket pkt = new CommPacket(DesktopMessage.MESSAGE_TYPE, this.svrId);
		pkt.setSerializableObject(msg);
		this.comm.dispatchPacket(pkt);
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void close()
	{
		this.comm.delete();
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD: CONNECT_SUCCESS
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//		
	private void msgHn_ControlAccepted(DesktopMessage msg)
	{
		Object obj = msg.getObject();
		if ((obj != null) && (obj instanceof IoParam)) {
			IoParam inPrm = (IoParam)obj;
			
			SwingUtilities.invokeLater(
				() -> this.viewer.controlAccepted(inPrm)
			);
		}
	}

//------------------------------------------------------------------------------
//  PRIVATE METHOD: CONNECT_FAILURE
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//		
	private void msgHn_ControlRefused(DesktopMessage msg)
	{
		SwingUtilities.invokeLater(
			() -> this.viewer.controlRefused(STR_CONTROL_REFUSED)
		);
	}

//------------------------------------------------------------------------------
//  PRIVATE METHOD: Illigal message
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void msgHn_illegalMessage(DesktopMessage msg, ObjectId srcId)
	{
		// PacketIoException �𐶐����C
		// �p�P�b�g���o�͂Ɋւ����O���������s����D
		PacketIoException ex = new PacketIoException(
			String.format(STR_ILLEGAL_MESSAGE, msg.getSubtype(), srcId)
		);
		SwingUtilities.invokeLater(
			() -> this.viewer.handleIllegalMessage(ex)
		);
	}
}
