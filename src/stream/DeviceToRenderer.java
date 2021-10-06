
package stream;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.DeviceInfo;
import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.viewer.VideoCanvas;

import util.ConsoleReader;
import util.SimpleViewer;

// Sample code of MidField System API
// Date Modified: 2021.09.19
//
public class DeviceToRenderer
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
			
			// ビデオとオーディオの入力デバイス情報リストを取得し，
			// 利用する入力デバイスを選択する．（ここでは最初の要素を選択する．）
			DeviceInfoManager devInfMgr = DeviceInfoManager.getInstance();
			DeviceInfo vidDev = devInfMgr.getInputVideoDeviceInfoList().get(0);
			DeviceInfo audDev = devInfMgr.getInputAudioDeviceInfoList().get(0);
			
			// SegmentIo の入力を入力デバイスとして構成する．
			SegmentIo segIo = new SegmentIo();
			segIo.configureInputDevice(vidDev, audDev);
			
			// SegmentIo の出力をデフォルトレンダラとして構成する．
			segIo.configureDefaultRenderer();
			
			// オプションの設定をする．
			segIo.setLiveSource(true);	// ライブソースオプションを有効にする．
			
			// SegmentIo をもとに StreamPerformer を生成する．
			pfmr = StreamPerformer.newInstance(segIo);	// SystemException, StreamException
			
			// StreamPerformer から VideoCanvas を取得する．
			VideoCanvas vidCvs = pfmr.getVideoCanvas();
			
			// StreamPerformer から VideoCanvas を取得し，SimpleViewer に追加する．
			SwingUtilities.invokeAndWait(() -> {
				new SimpleViewer("Device to Renderer", vidCvs);
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