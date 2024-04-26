
package rpc.remocon;

import static com.midfield_system.rpc.api.server.ParamName.AUDIO_DEVICE_INDEX;
import static com.midfield_system.rpc.api.server.ParamName.AUDIO_FORMAT_INDEX;
import static com.midfield_system.rpc.api.server.ParamName.CONNECTION_MODE;
import static com.midfield_system.rpc.api.server.ParamName.IS_LIVE_SOURCE;
import static com.midfield_system.rpc.api.server.ParamName.MIXER_NAME;
import static com.midfield_system.rpc.api.server.ParamName.MULTICAST_ADDRESS;
import static com.midfield_system.rpc.api.server.ParamName.PROTOCOL_TYPE;
import static com.midfield_system.rpc.api.server.ParamName.STREAM_INFO_INDEX;
import static com.midfield_system.rpc.api.server.ParamName.VIDEO_DEVICE_INDEX;
import static com.midfield_system.rpc.api.server.ParamName.VIDEO_FORMAT_INDEX;

import java.util.Map;
import java.util.TreeMap;

import com.midfield_system.rpc.api.RequestObject;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.11
 *
 */
public class CltSegmentIo
{
// =============================================================================
// INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    public CltSegmentIo()
    {
        //
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject reset(String[] args)
    {
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], null);
        return rpcReq;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC: 入力設定（入力デバイス）
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject configureInputDevice(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC要求に必要となる引数の数を確認する．
        if (args.length >= 5) {
            // RPC要求用のマップを生成する．
            map = new TreeMap<String, Object>();
            
            // デバイスとフォーマットのインデックス値をマップに設定する．
            map.put(VIDEO_DEVICE_INDEX, args[1]);
            map.put(VIDEO_FORMAT_INDEX, args[2]);
            map.put(AUDIO_DEVICE_INDEX, args[3]);
            map.put(AUDIO_FORMAT_INDEX, args[4]);
        }
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], map);
        return rpcReq;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC: 入力設定（受信ストリーム）
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject configureIncomingStream(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC要求に必要となる引数の数を確認する．
        if (args.length >= 2) {
            // RPC要求用のマップを生成する．
            map = new TreeMap<String, Object>();
            
            // ストリーム情報のインデックス値をマップに設定する．
            map.put(STREAM_INFO_INDEX, args[1]);
        }
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], map);
        return rpcReq;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC: 入力設定（ミキサー）
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject configureStreamingMixer(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC要求に必要となる引数の数を確認する．
        if (args.length >= 2) {
            // RPC要求用のマップを生成する．
            map = new TreeMap<String, Object>();
            
            // ストリーム情報のインデックス値をマップに設定する．
            map.put(MIXER_NAME, args[1]);
        }
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], map);
        return rpcReq;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC: 出力設定（送信ストリーム）
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject getOutputVideoFormatList(String[] args)
    {
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], null);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject getOutputAudioFormatList(String[] args)
    {
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], null);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject configureOutgoingStream(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC要求に必要となる引数の数を確認する．
        if (args.length >= 3) {
            // RPC要求用のマップを生成する．
            map = new TreeMap<String, Object>();
            
            // ビデオとオーディオフォーマットのインデックス値をマップに設定する．
            map.put(VIDEO_FORMAT_INDEX, args[1]);
            map.put(AUDIO_FORMAT_INDEX, args[2]);
        }
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], map);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject setTransportProtocol(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC要求に必要となる引数の数を確認する．
        if (args.length >= 3) {
            // RPC要求用のマップを生成する．
            map = new TreeMap<String, Object>();
            
            // TCPを利用するか否か，およびコネクションモードの指定を
            // マップに設定する．
            map.put(PROTOCOL_TYPE, args[1]);
            map.put(CONNECTION_MODE, args[2]);
        }
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], map);
        return rpcReq;
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject setMulticastAddress(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC要求に必要となる引数の数を確認する．
        if (args.length >= 2) {
            // RPC要求用のマップを生成する．
            map = new TreeMap<String, Object>();
            
            // マルチキャストアドレスをマップに設定する．
            map.put(MULTICAST_ADDRESS, args[1]);
        }
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], map);
        return rpcReq;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC: 出力設定（ミキサーへの入力）
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject configureMixerInput(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC要求に必要となる引数の数を確認する．
        if (args.length >= 2) {
            // RPC要求用のマップを生成する．
            map = new TreeMap<String, Object>();
            
            // ストリーム情報のインデックス値をマップに設定する．
            map.put(MIXER_NAME, args[1]);
        }
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], map);
        return rpcReq;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC: 出力設定（レンダラ）
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject configurePreferredRenderer(String[] args)
    {
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], null);
        return rpcReq;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC: プレビューワの設定
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject setPreferredPreviewer(String[] args)
    {
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], null);
        return rpcReq;
    }
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: RPC: 各種設定
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public RequestObject setLiveSource(String[] args)
    {
        Map<String, Object> map = null;
        
        // RPC要求に必要となる引数の数を確認する．
        if (args.length >= 2) {
            // RPC要求用のマップを生成する．
            map = new TreeMap<String, Object>();
            
            // ライブソースであるか否かをマップに設定する．
            map.put(IS_LIVE_SOURCE, args[1]);
        }
        // RPC要求を生成して返す．
        RequestObject rpcReq = RequestObject.createRequest(args[0], map);
        return rpcReq;
    }
}