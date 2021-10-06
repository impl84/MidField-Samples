
package stream;

import java.io.IOException;
import java.util.List;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamInfoManager;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.viewer.VideoCanvas;
import com.midfield_system.protocol.StreamInfo;

import util.ConsoleReader;
import util.SimpleViewer;

// Sample code of MidField System API
// Date Modified: 2021.09.19
//
public class NetworkToNetwork
{
	public static void main(String[] args)
	{
		MfsNode mfs = null;
		StreamPerformer pfmr = null;
		ConsoleReader reader = ConsoleReader.getInstance();
		
		try {
			// MidField System を初期化し，起動する．
			mfs = MfsNode.initialize();		// SystemException
			mfs.activate();					// SystemException
			
			// 送信ホスト名/IPアドレスをコマンドラインから取得する．
			System.out.print("  送信ホスト名/IPアドレス：");
			String srcAddr = reader.readLine();	// IOException
			
			// ストリーム情報リストを送信ホストから取得する．
			StreamInfoManager stmInfMgr = StreamInfoManager.getInstance();
			List<StreamInfo> lsStmInf = stmInfMgr.fetchSourceStreamInfoList(srcAddr);
			if (lsStmInf.size() <= 0) {
				throw new IOException("  ※受信可能なストリームがありません．");
			}
			// SegmentIo の入力を受信ストリームとして構成する．
			SegmentIo segIo = new SegmentIo();
			segIo.configureIncomingStream(lsStmInf.get(0));
			
			// SegmentIo の出力を送信ストリームとして構成し，
			// トランスポートプロトコルの設定を行う．
			segIo.configureOutgoingStream(
				segIo.getOutputVideoFormatList().get(0),
				segIo.getOutputAudioFormatList().get(0)
			);
			segIo.setTransportProtocol(
				ProtocolType.TCP,		// TCPを利用する．
				ConnectionMode.PASSIVE	// コネクション接続要求を受け入れる．
			);
			// オプションの設定をする．
			segIo.setPreviewer();		// プレビューワ―を利用する．
			segIo.setLiveSource(true);	// ライブソースオプションを有効にする．	
			
			// SegmentIo をもとに StreamPerformer を生成する．
			pfmr = StreamPerformer.newInstance(segIo);	// SystemException, StreamException
			
			// StreamPerformer から VideoCanvas を取得する．
			VideoCanvas vidCvs = pfmr.getVideoCanvas();
			
			// StreamPerformer から VideoCanvas を取得し，SimpleViewer に追加する．
			SwingUtilities.invokeAndWait(() -> {
				new SimpleViewer("Network to Network", vidCvs);
			});
				// InterruptedException, InvocationTargetException
			
			// 入出力処理を開始する．
			pfmr.open();	// StreamException
			pfmr.start();	// StreamException
			
			// Enterキーの入力を待つ．
			System.out.print("> Enter キーの入力を待ちます．");
			reader.readLine();	// IOException
			
			// 入出力処理を終了する．
			pfmr.stop();	// StreamException
			pfmr.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			// StreamPerformer, MidField System を終了する．
			if (pfmr != null) { pfmr.delete(); }
			if (mfs != null) { mfs.shutdown(); }
			
			// ConsoleReader を解放する．
			reader.release();
		}
	}
}