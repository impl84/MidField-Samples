
package communicator;

import static communicator.TwoWayCommunication.ASYNC_INTERVIEW_REQUEST;
import static communicator.TwoWayCommunication.ASYNC_INTERVIEW_RESPONSE;
import static communicator.TwoWayCommunication.INTERVIEW_REQUEST;
import static communicator.TwoWayCommunication.INTERVIEW_RESPONSE;

import com.midfield_system.api.system.CommPacket;
import com.midfield_system.api.system.CommPacketHandler;
import com.midfield_system.api.system.ObjectId;
import com.midfield_system.api.system.PacketCommunicator;
import com.midfield_system.api.system.PacketIoException;

import util.ConsolePrinter;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: Responder 
 *
 * Date Modified: 2021.10.27
 *
 */
public class Responder
	implements	CommPacketHandler
{
	// - PUBLIC CONSTANT VALUE -------------------------------------------------
	public static final String COMMUNICATOR_NAME = "Responder";
	
// =============================================================================
//  INSTANCE VARIABLE:
// =============================================================================
	
	// - PRIVATE VARIABLE ------------------------------------------------------
	private final ConsolePrinter printer;
	
	private final PacketCommunicator comm;
	
// =============================================================================
//  INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
//  PUBLIC METHOD: IMPREMENTS: CommPacketHandler
// -----------------------------------------------------------------------------
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPREMENTS: CommPacketHandler
	//
	@Override
	public boolean handleIncomingPacket(CommPacket inPkt)
	{
		boolean handled = true;
		
		// ���b�Z�[�W�^�C�v���̏��������s����D
		String type = inPkt.getMessageType();
		switch (type) {
		case ASYNC_INTERVIEW_REQUEST : msgHn_AsyncInterviewRequest(inPkt);	break;
		case INTERVIEW_REQUEST		 : msgHn_InterviewRequest(inPkt);		break;
		default						 : msgHn_UnsupportedMessage(inPkt);		break;
		}
		return handled;
	}
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPREMENTS: CommPacketHandler
	//
	@Override
	public void handlePacketIoException(PacketIoException ex)
	{
		ex.printStackTrace();
	}
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPREMENTS: CommPacketHandler
	//
	@Override
	public void handleUnreachablePacket(CommPacket inPkt)
	{
		this.printer.println("�����B�p�P�b�g�F" + inPkt.getMessageType());
	}
	
// -----------------------------------------------------------------------------
//  PACKAGE METHOD:
// -----------------------------------------------------------------------------
	
	// - CONSTRUCTOR -----------------------------------------------------------
	//
	Responder(ConsolePrinter printer)
	{
		this.printer = printer;
		
		// PacketCommunicator �𐶐�����D
		this.comm = new PacketCommunicator(COMMUNICATOR_NAME, this);
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	void close()
	{
		// PacketCommunicator ���폜����D
		this.comm.close();
	}
	
// -----------------------------------------------------------------------------
//  PRIVATE METHOD: 
// -----------------------------------------------------------------------------
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private void msgHn_AsyncInterviewRequest(CommPacket reqPkt)
	{
		// �����p�P�b�g�̈���Ƃ��āC���̓p�P�b�g�̑��M��ID���擾����D
		ObjectId peerId = reqPkt.getSourceObjectId();
		
		// ���̓p�P�b�g����v�����b�Z�[�W���擾����D
		RequestMessage reqMsg = reqPkt.getPayload(RequestMessage.class);
		
		// �����p�P�b�g�Ɖ������b�Z�[�W�𐶐�����D
		CommPacket resPkt = new CommPacket(ASYNC_INTERVIEW_RESPONSE, peerId);
		ResponseMessage resMsg = new ResponseMessage(
			"�񓚎�",
			reqMsg.getInterviewer() + "����ł��ˁC����ɂ��́D"
		);
		resPkt.setPayload(resMsg);
		
		// �����p�P�b�g�𑗐M����D
		this.comm.dispatchPacket(resPkt);
	}
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private void msgHn_InterviewRequest(CommPacket reqPkt)
	{
		// �����p�P�b�g�̈���Ƃ��āC���̓p�P�b�g�̑��M��ID���擾����D
		ObjectId peerId = reqPkt.getSourceObjectId();
		
		// ���̓p�P�b�g����v�����b�Z�[�W���擾����D
		RequestMessage reqMsg = reqPkt.getPayload(RequestMessage.class);
		
		// �����p�P�b�g�Ɖ������b�Z�[�W�𐶐�����D
		CommPacket resPkt = new CommPacket(INTERVIEW_RESPONSE, peerId);
		ResponseMessage resMsg = new ResponseMessage(
			"�񓚎�",
			reqMsg.getInterviewer() + "����ł��ˁC�ǂ����͂��߂܂��āD"
		);
		resPkt.setPayload(resMsg);
		
		// �v���p�P�b�g���̗v���ԍ����C�����ԍ��Ƃ��Đݒ肷��D
		resPkt.setResponseNumber(reqPkt.getRequestNumber());
		
		// �����p�P�b�g�𑗐M����D
		this.comm.dispatchPacket(resPkt);
	}
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private void msgHn_UnsupportedMessage(CommPacket inPkt)
	{
		this.printer.println("���Ή��p�P�b�g�F" + inPkt.getMessageType());
	}
}
