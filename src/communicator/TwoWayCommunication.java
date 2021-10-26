
package communicator;

import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.system.SystemProperty;

import util.ConsolePrinter;
import util.ConsoleReader;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: TwoWayCommunication 
 *
 * Date Modified: 2021.10.27
 *
 */

//==============================================================================
public class TwoWayCommunication
{
	//- PACKAGE CONSTANT VALUE -------------------------------------------------
	static final String ASYNC_INTERVIEW_REQUEST		= "Async-Interview-Request";
	static final String ASYNC_INTERVIEW_RESPONSE	= "Async-Interview-Response";
	static final String INTERVIEW_REQUEST			= "Interview-Request";
	static final String INTERVIEW_RESPONSE			= "Interview-Response";
	
//------------------------------------------------------------------------------
//  PUBLIC STATIC METHOD:
//------------------------------------------------------------------------------
	
	//- PUBLIC STATIC METHOD ---------------------------------------------------
	//
	public static void main(String[] args)
	{
		// コンソールを利用した文字の入出力の準備をする．
		ConsoleReader reader = ConsoleReader.getInstance();
		ConsolePrinter printer = ConsolePrinter.getInstance();
		
		MfsNode mfs = null;
		try {
			// MidField System を初期化し，起動する．
			printer.println("> MidField System を起動します．");
			mfs = MfsNode.initialize();	// SystemException
			mfs.activate();				// SystemException
			
			// MidField System が利用しているローカルIPアドレスを取得する．
			SystemProperty sp = SystemProperty.getCurrentProperty();
			String localIpAddr = sp.getSysLocalIpAddrWoScopeId();
			
			// Responder と Interviewer を生成する．
			Responder responder = new Responder(printer);
			Interviewer interviewer = new Interviewer(printer, localIpAddr);
			
			// Interviewer が Responder へ要求パケットを送信し，
			// Responder が Interviewer へ応答パケットを返信する．
			// ※Interviewer は要求パケットを送信した後，
			//   異なるスレッドで返信を処理する．
			interviewer.asyncInterview();
			
			// Interviewer が Responder へ要求パケットを送信し，
			// Responder が Interviewer へ応答パケットを返信する．
			// ※Interviewer は要求パケットを送信した後，
			//   同じスレッドで返信が届くまで待ち，返信を処理する．
			interviewer.interview();
			
			// Enter キーの入力を待つ．
			printer.println("> Enter キーを押してください．");
			reader.readLine();
			
			// Interviewer と Responder を終了する．
			interviewer.close();
			responder.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			// MidField System を終了する．
			if (mfs != null) {
				System.out.println("> MidField System を終了します．");
				mfs.shutdown();
			}
			// 標準入力からの1行読み込みに利用した ConsoleReader を解放する．
			reader.release();
		}
	}
}
