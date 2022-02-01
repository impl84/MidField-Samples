
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
        
        // メッセージタイプ毎の処理を実行する．
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
        this.printer.println("未到達パケット：" + inPkt.getMessageType());
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    Interviewer(ConsolePrinter printer, String peerAddr)
    {
        this.printer = printer;
        
        // PacketCommunicator を生成する．
        this.comm = new PacketCommunicator(COMMUNICATOR_NAME, this);
        
        // メッセージの宛先となる ObjectId を生成する．
        this.peerId = new ObjectId(Responder.COMMUNICATOR_NAME, peerAddr);
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void asyncInterview()
    {
        // 要求パケットと要求メッセージを生成する．
        CommPacket     reqPkt = new CommPacket(ASYNC_INTERVIEW_REQUEST, this.peerId);
        RequestMessage reqMsg = new RequestMessage(
            "インタビュアーＡ",
            "こんにちは．"
        );
        reqPkt.setPayload(reqMsg);
        
        // 要求メッセージをコンソールへ出力する．
        this.printer.println(reqMsg);
        
        // 要求パケットを送信する．
        this.comm.dispatchPacket(reqPkt);
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void interview()
    {
        try {
            // 要求パケットと要求メッセージを生成する．
            CommPacket     reqPkt = new CommPacket(INTERVIEW_REQUEST, this.peerId);
            RequestMessage reqMsg = new RequestMessage(
                "インタビュアーＢ",
                "はじめまして．"
            );
            reqPkt.setPayload(reqMsg);
            
            // 要求メッセージをコンソールへ出力する．
            this.printer.println(reqMsg);
            
            // 要求パケットを送信して，応答パケットを受信する．
            CommPacket resPkt = this.comm.dispatchRequest(reqPkt, INTERVIEW_RESPONSE);
            // SystemException (PacketIoException, RemoteException),
            // TimeoutException, InterruptedException
            
            // 応答パケットから応答メッセージを取得して表示する．
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
        // PacketCommunicator を削除する．
        this.comm.close();
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void msgHn_AsyncInterviewResponse(CommPacket resPkt)
    {
        // 応答パケットから応答メッセージを取得して表示する．
        ResponseMessage resMsg = resPkt.getPayload(ResponseMessage.class);
        this.printer.println(resMsg);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void msgHn_UnsupportedMessage(CommPacket inPkt)
    {
        this.printer.println("未対応パケット：" + inPkt.getMessageType());
    }
}
