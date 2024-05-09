
package rpc.remocon;

import static com.midfield_system.api.util.Constants.STR_DOT;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.midfield_system.rpc.api.RequestObject;
import com.midfield_system.rpc.core.InvocableMethod;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2022.06.08
 *
 */
class CommandParser
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    static final String ARG_SEPARATOR = "\\s+";	//$NON-NLS-1$
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private Map<String, InvocableMethod> methodMap = null;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    CommandParser()
    {
        // �R�}���h�� RequestObject �C���X�^���X�ɕϊ����邽�߂�
        // �p�[�T�N���X�̃C���X�^���X�𐶐�����D
        CltMfsNode           cltMfsNode = new CltMfsNode();
        CltDeviceInfoManager cltDiMgr   = new CltDeviceInfoManager();
        CltStreamInfoManager cltSiMgr   = new CltStreamInfoManager();
        CltSegmentIo         cltSegIo   = new CltSegmentIo();
        CltStreamPerformer   cltStmPfmr = new CltStreamPerformer();
        
        // �p�[�T�N���X�̃C���X�^���X��z��ɂ���D
        Object[] parserInstanceArray = {
            cltMfsNode,
            cltDiMgr,
            cltSiMgr,
            cltSegIo,
            cltStmPfmr,
        };
        // �p�[�T�p���\�b�h�����L�[�Ƃ��C
        // InvocableMethod �C���X�^���X��l�Ƃ���}�b�v�𐶐�����D
        this.methodMap = new TreeMap<String, InvocableMethod>();
        
        // �p�[�T�N���X�̃��\�b�h���}�b�v�ɓo�^����D
        for (int i = 0; i < parserInstanceArray.length; i++) {
            registerMethod(parserInstanceArray[i], this.methodMap);
        }
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    Set<String> getMethodNameSet()
    {
        Set<String> nameSet = this.methodMap.keySet();
        return nameSet;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    RequestObject parseCommand(String command)
        throws InvocationTargetException,
            IllegalAccessException
    {
        // �R�}���h���Z�p���[�^�ŋ�؂�C������̔z��ɂ���D
        String[] args = command.split(ARG_SEPARATOR);
        if ((args == null) || (args.length < 1)) {
            // RPC�v���ɕϊ��ł�����͖������C
            // RequestObject �𐶐����C
            // �����RPC�v��(JSON������)�ɕϊ����Ė߂�D
            RequestObject rpcReq = requestNothing();
            return rpcReq;
        }
        // �R�}���h���̃��\�b�h���ɑΉ����� InvocableMethod �C���X�^���X���擾����D
        String          methodName = args[0];
        InvocableMethod method     = this.methodMap.get(methodName);
        if (method == null) {
            // ���\�b�h�����烁�\�b�h�C���X�^���X���擾�ł��Ȃ����C
            // RequestObject �𐶐����C
            // �����RPC�v��(JSON������)�ɕϊ����Ė߂�D
            RequestObject rpcReq = unknownMethod(methodName);
            return rpcReq;
        }
        // �Ή����郁�\�b�h���Ăяo���CRPC�v���𐶐�����D
        RequestObject rpcReq = (RequestObject)method.invoke(args);
        // InvocationTargetException, IllegalAccessException
        
        // RPC�v����Ԃ��D
        return rpcReq;
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void registerMethod(Object parserObject, Map<String, InvocableMethod> map)
    {
        // �p�[�T�I�u�W�F�N�g�̃N���X�I�u�W�F�N�g���擾����D
        Class<?> parserClass = parserObject.getClass();
        
        // �p�[�T�N���X���̃��\�b�h�̔z����擾����D
        Method[] methods = parserClass.getMethods();
        
        // �p�[�T�N���X���̃��\�b�h�𑖍�����D
        for (int i = 0; i < methods.length; i++) {
            // ���̃��\�b�h���C�^����ꂽ�p�[�T�N���X���Œ�`����Ă���
            // ���\�b�h�ł��邱�Ƃ��m�F����D
            Class<?> declaringClass = methods[i].getDeclaringClass();
            if (parserClass.equals(declaringClass) == false) {
                continue;
            }
            // ���̃��\�b�h�� public �ł��邩���m�F����D
            if ((methods[i].getModifiers() & Modifier.PUBLIC) != 1) {
                continue;
            }
            // ���̃��\�b�h�� RequestObject ��Ԃ����Ƃ��m�F����D
            Class<?> returnType = methods[i].getReturnType();
            if (returnType.equals(RequestObject.class) == false) {
                continue;
            }
            // ���̃��\�b�h�̈����� 1�ł��邱�Ƃ��m�F����D
            Class<?>[] paramTypes = methods[i].getParameterTypes();
            if (paramTypes.length != 1) {
                continue;
            }
            // �����̌^�� String[] �ł��邱�Ƃ��m�F����D
            Class<?> paramClass = paramTypes[0];
            if (paramClass.equals(String[].class) == false) {
                continue;
            }
            // �o�^���郁�\�b�h�̃N���X���Ƃ��āC
            // �uClt�v�Ŏn�܂�N���X���́uClt�v(3����)����菜����������𐶐�����D
            String name = declaringClass.getSimpleName().substring(3);
            
            // �N���X���t���̃��\�b�h���𐶐�����D
            name = name.concat(STR_DOT);
            name = name.concat(methods[i].getName());
            
            // ���\�b�h�� InvocableMethod �̃C���X�^���X���}�b�v�֓o�^����D
            InvocableMethod method = new InvocableMethod(methods[i], parserObject);
            map.put(name, method);
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private RequestObject requestNothing()
    {
        // ���\�b�h��������RPC�v���𐶐�����D
        RequestObject rpcReq = RequestObject.createRequest(null, null);
        
        // RPC�v����Ԃ��D
        return rpcReq;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private RequestObject unknownMethod(String methodName)
    {
        // RPC�v���𐶐�����D
        RequestObject rpcReq = RequestObject.createRequest(methodName, null);
        
        // RPC�v����Ԃ��D
        return rpcReq;
    }
}
