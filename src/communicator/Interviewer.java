
package communicator;

import static communicator.TwoWayCommunication.ASYNC_INTERVIEW_REQUEST;
import static communicator.TwoWayCommunication.ASYNC_INTERVIEW_RESPONSE;
import static communicator.TwoWayCommunication.INTERVIEW_REQUEST;
import static communicator.TwoWayCommunication.INTERVIEW_RESPONSE;

import java.util.concurrent.TimeoutException;

import com.midfield_system.api.system.CommPacket;
import com.midfield_system.api.system.CommPacketHandler;
import com.midfield_system.api.system.ObjectId;
import com.midfield_system.api.system.PacketCommunicator;
import com.midfield_system.api.system.PacketIoException;
import com.midfield_system.api.system.SystemException;

import util.ConsolePrinter;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: Interviewer
 *
 * Date Modified: 2021.10.27
 *
 */
public class Interviewer
    implements
        CommPacketHandler
{
    // - PUBLIC CONSTANT VALUE -------------------------------------------------
    public static final String COMMUNICATOR_NAME = "Interviewer";
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final ConsolePrinter printer;
    
    private final PacketCommunicator comm;
    private final ObjectId           peerId;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: IMPREMENTS: CommPacketHandler
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
        case ASYNC_INTERVIEW_RESPONSE:
            msgHn_AsyncInterviewResponse(inPkt);
            break;
        default:
            msgHn_UnsupportedMessage(inPkt);
            break;
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
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    Interviewer(ConsolePrinter printer, String peerAddr)
    {
        this.printer = printer;
        
        // PacketCommunicator �𐶐�����D
        this.comm = new PacketCommunicator(COMMUNICATOR_NAME, this);
        
        // ���b�Z�[�W�̈���ƂȂ� ObjectId �𐶐�����D
        this.peerId = new ObjectId(Responder.COMMUNICATOR_NAME, peerAddr);
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void asyncInterview()
    {
        // �v���p�P�b�g�Ɨv�����b�Z�[�W�𐶐�����D
        CommPacket     reqPkt = new CommPacket(ASYNC_INTERVIEW_REQUEST, this.peerId);
        RequestMessage reqMsg = new RequestMessage(
            "�C���^�r���A�[�`",
            "����ɂ��́D"
        );
        reqPkt.setPayload(reqMsg);
        
        // �v�����b�Z�[�W���R���\�[���֏o�͂���D
        this.printer.println(reqMsg);
        
        // �v���p�P�b�g�𑗐M����D
        this.comm.dispatchPacket(reqPkt);
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void interview()
    {
        try {
            // �v���p�P�b�g�Ɨv�����b�Z�[�W�𐶐�����D
            CommPacket     reqPkt = new CommPacket(INTERVIEW_REQUEST, this.peerId);
            RequestMessage reqMsg = new RequestMessage(
                "�C���^�r���A�[�a",
                "�͂��߂܂��āD"
            );
            reqPkt.setPayload(reqMsg);
            
            // �v�����b�Z�[�W���R���\�[���֏o�͂���D
            this.printer.println(reqMsg);
            
            // �v���p�P�b�g�𑗐M���āC�����p�P�b�g����M����D
            CommPacket resPkt = this.comm.dispatchRequest(reqPkt, INTERVIEW_RESPONSE);
            // SystemException (PacketIoException, RemoteException),
            // TimeoutException, InterruptedException
            
            // �����p�P�b�g���牞�����b�Z�[�W���擾���ĕ\������D
            ResponseMessage resMsg = resPkt.getPayload(ResponseMessage.class);
            this.printer.println(resMsg);
        }
        catch (
            SystemException | TimeoutException | InterruptedException ex
        ) {
            ex.printStackTrace();
        }
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void close()
    {
        // PacketCommunicator ���폜����D
        this.comm.close();
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void msgHn_AsyncInterviewResponse(CommPacket resPkt)
    {
        // �����p�P�b�g���牞�����b�Z�[�W���擾���ĕ\������D
        ResponseMessage resMsg = resPkt.getPayload(ResponseMessage.class);
        this.printer.println(resMsg);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void msgHn_UnsupportedMessage(CommPacket inPkt)
    {
        this.printer.println("���Ή��p�P�b�g�F" + inPkt.getMessageType());
    }
}
