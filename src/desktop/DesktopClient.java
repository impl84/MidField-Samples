
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
		STR_UNREACHABLE		= "サーバへ送信したパケットが到達できませんでした",
		STR_CONTROL_REFUSED	= "遠隔操作要求が拒否されました．",
		STR_ILLEGAL_MESSAGE	= "不正なメッセージを受信しました．: %s from %s\n";
	
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
		
		// 入力パケットが DesktopMessage 用のパケットであることを確認する．
		String msgType = pkt.getMessageType();
		if (msgType.equals(DesktopMessage.MESSAGE_TYPE) == false) {
			wasHandled = false;
			return wasHandled;
		}
		// パケットから DesktopMessage のインスタンスを取得する．
		DesktopMessage msg = pkt.getSerializableObject(DesktopMessage.class);
		if (msg == null) {
			wasHandled = false;
			return wasHandled;
		}
		// 送信元の ObjectId を取得する．
		ObjectId srcId = pkt.getSourceObjectId();
		
		// メッセージのサブタイプ毎の処理を実行する．
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
		// パケット入出力に関する例外処理を実行する．
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
		// PacketIoException を生成し，
		// パケット入出力に関する例外処理を実行する．
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
		// DesktopViewer の参照を保持する．
		this.viewer = viewer;
		
		// PacketCommunicator を生成する．
		String name = new String(DESKTOP_CLIENT + DesktopClient.instanceNumber++);
		this.comm = new PacketCommunicator(name, this);
			// SystemException
		
		// メッセージ配送先の ObjectId を生成する．
		MfsNode mfs = MfsNode.getInstance();
		UUID svrNodeId = mfs.resolveNodeId(svrAddr);
			// SystemException
		
		this.svrId = new ObjectId(DesktopServer.DESKTOP_SERVER, svrNodeId);
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void startControl(String dstAddr)
	{
		// 遠隔操作開始メッセージを配送する．
		DesktopMessage msg = new DesktopMessage(Subtype.START_CONTROL, null);
		CommPacket pkt = new CommPacket(DesktopMessage.MESSAGE_TYPE, this.svrId);
		pkt.setSerializableObject(msg);
		this.comm.dispatchPacket(pkt);
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void dispatchEvent(DesktopMessage.Action action, Serializable obj)
	{
		// 遠隔操作メッセージを配送する．
		DesktopMessage msg = new DesktopMessage(Subtype.CONTROL_MESSAGE, action, obj);
		CommPacket pkt = new CommPacket(DesktopMessage.MESSAGE_TYPE, this.svrId);
		pkt.setSerializableObject(msg);
		this.comm.dispatchPacket(pkt);
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void stopControl()
	{
		// 遠隔操作停止メッセージを配送する．
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
		// PacketIoException を生成し，
		// パケット入出力に関する例外処理を実行する．
		PacketIoException ex = new PacketIoException(
			String.format(STR_ILLEGAL_MESSAGE, msg.getSubtype(), srcId)
		);
		SwingUtilities.invokeLater(
			() -> this.viewer.handleIllegalMessage(ex)
		);
	}
}
