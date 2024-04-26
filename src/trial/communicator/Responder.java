
package trial.communicator;

import static trial.communicator.TwoWayCommunication.ASYNC_INTERVIEW_REQUEST;
import static trial.communicator.TwoWayCommunication.ASYNC_INTERVIEW_RESPONSE;
import static trial.communicator.TwoWayCommunication.INTERVIEW_REQUEST;
import static trial.communicator.TwoWayCommunication.INTERVIEW_RESPONSE;

import com.midfield_system.api.system.CommPacket;
import com.midfield_system.api.system.CommPacketHandler;
import com.midfield_system.api.system.ObjectId;
import com.midfield_system.api.system.PacketCommunicator;
import com.midfield_system.api.system.PacketIoException;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2021.10.27
 *
 */
public class Responder
    implements
        CommPacketHandler
{
    // - PUBLIC CONSTANT VALUE -------------------------------------------------
    public static final String COMMUNICATOR_NAME = "Responder";
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private final ConsolePrinter printer;
    
    private final PacketCommunicator comm;
    
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
        
        // メッセージタイプ毎の処理を実行する．
        String type = inPkt.getMessageType();
        switch (type) {
        case ASYNC_INTERVIEW_REQUEST:
            msgHn_AsyncInterviewRequest(inPkt);
            break;
        case INTERVIEW_REQUEST:
            msgHn_InterviewRequest(inPkt);
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
        this.printer.println("未到達パケット：" + inPkt.getMessageType());
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    Responder(ConsolePrinter printer)
    {
        this.printer = printer;
        
        // PacketCommunicator を生成する．
        this.comm = new PacketCommunicator(COMMUNICATOR_NAME, this);
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void close()
    {
        // PacketCommunicator を削除する．
        this.comm.close();
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void msgHn_AsyncInterviewRequest(CommPacket reqPkt)
    {
        // 応答パケットの宛先として，入力パケットの送信元IDを取得する．
        ObjectId peerId = reqPkt.getSourceObjectId();
        
        // 入力パケットから要求メッセージを取得する．
        RequestMessage reqMsg = reqPkt.getPayload(RequestMessage.class);
        
        // 応答パケットと応答メッセージを生成する．
        CommPacket      resPkt = new CommPacket(ASYNC_INTERVIEW_RESPONSE, peerId);
        ResponseMessage resMsg = new ResponseMessage(
            "回答者",
            reqMsg.getInterviewer() + "さんですね，こんにちは．"
        );
        resPkt.setPayload(resMsg);
        
        // 応答パケットを送信する．
        this.comm.dispatchPacket(resPkt);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void msgHn_InterviewRequest(CommPacket reqPkt)
    {
        // 応答パケットの宛先として，入力パケットの送信元IDを取得する．
        ObjectId peerId = reqPkt.getSourceObjectId();
        
        // 入力パケットから要求メッセージを取得する．
        RequestMessage reqMsg = reqPkt.getPayload(RequestMessage.class);
        
        // 応答パケットと応答メッセージを生成する．
        CommPacket      resPkt = new CommPacket(INTERVIEW_RESPONSE, peerId);
        ResponseMessage resMsg = new ResponseMessage(
            "回答者",
            reqMsg.getInterviewer() + "さんですね，どうもはじめまして．"
        );
        resPkt.setPayload(resMsg);
        
        // 要求パケット内の要求番号を，応答番号として設定する．
        resPkt.setResponseNumber(reqPkt.getRequestNumber());
        
        // 応答パケットを送信する．
        this.comm.dispatchPacket(resPkt);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void msgHn_UnsupportedMessage(CommPacket inPkt)
    {
        this.printer.println("未対応パケット：" + inPkt.getMessageType());
    }
}
