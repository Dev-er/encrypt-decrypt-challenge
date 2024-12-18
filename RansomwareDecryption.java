import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;

public class RansomwareDecryption {

    public static void main(String[] args) {
        try {
            // Nome do arquivo criptografado
            String encryptedFileName = "teste.txt.ransomwaretroll";  // Arquivo criptografado
            String decryptedFileName = "teste.txt";  // Arquivo descriptografado

            // Verifique se o arquivo criptografado existe
            File encryptedFile = new File(encryptedFileName);
            if (!encryptedFile.exists()) {
                System.err.println("O arquivo criptografado não foi encontrado: " + encryptedFileName);
                return;
            }

            // Ler o arquivo criptografado como dados binários
            byte[] fileData = Files.readAllBytes(encryptedFile.toPath());  // Lê os bytes do arquivo criptografado

            // Chave de descriptografia (deve ser de 16 bytes)
            byte[] keyBytes = "testeransomwares".getBytes();  // Chave de 16 bytes
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            // Criar objeto Cipher para AES no modo ECB (sem padding)
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            // Se a entrada não for um múltiplo de 16, podemos adicionar padding manualmente
            int blockSize = cipher.getBlockSize();
            int paddingLength = blockSize - (fileData.length % blockSize);
            if (paddingLength != blockSize) {
                byte[] paddedFileData = new byte[fileData.length + paddingLength];
                System.arraycopy(fileData, 0, paddedFileData, 0, fileData.length);
                fileData = paddedFileData;  // Substitui os dados originais com padding
            }

            // Descriptografar os dados
            byte[] decryptData = cipher.doFinal(fileData);

            // Criar um novo arquivo com os dados descriptografados como dados binários
            try (FileOutputStream newFile = new FileOutputStream(decryptedFileName)) {
                newFile.write(decryptData);  // Escreve os bytes descriptografados
            }

            System.out.println("Descriptografia concluída com sucesso!");

        } catch (FileNotFoundException e) {
            // Captura caso o arquivo criptografado não seja encontrado
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            // Captura de exceções de IO
            System.err.println("Erro de leitura ou gravação de arquivo: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Captura de outras exceções
            System.err.println("Erro geral: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
