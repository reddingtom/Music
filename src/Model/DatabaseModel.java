package Model;

import Control.PopUp;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Kaik D' Andrade
 * @author Gabriel Souza // Read // Update
 */
public class DatabaseModel {

    private Connection conn = null;
    private PreparedStatement pstm = null;
    private ResultSet res = null;
    public String sql = null;
    private static final String URL = "jdbc:mysql://localhost:3306/teste?user=root&password=";

    public void conectDb() {
        try {
            // Inicializa a conexão se ocorrer nenhum error
            setConn(DriverManager.getConnection(URL));
        } catch (SQLException error) {
            // Caso gere um erro
            PopUp.showWarning(error);
        }
    }

    public void closeDb() {
        // Zera as variáveis referentes ao banco de dados
        if (getRes() != null) try {
            getRes().close();
        } catch (SQLException ignore) {
        }

        if (getPstm() != null) try {
            getPstm().close();
        } catch (SQLException ignore) {
        }

        if (getConn() != null) try {
            getConn().close();
        } catch (SQLException ignore) {
        }
    }

    // @author Gabriel Souza
    public void createUser(String userName, String userEmail, String userPass, String userType) {

        // Comando SQL
        sql = "INSERT INTO user(u_name, u_email, u_passwd, u_type) VALUES (?, ?, sha2(?, 512), ?)";

        try {
            // Conecta ao banco de dados, depois prepara, filtra e sanitiza o sql para executa-lo
            conectDb();
            setPstm(getConn().prepareStatement(sql));

            // Alterando os "?" pelos valores corretos
            getPstm().setString(1, userName);
            getPstm().setString(2, userEmail);
            getPstm().setString(3, userPass);
            getPstm().setString(4, userType);

            // Executa o comando SQL no banco de dados
            getPstm().execute();

            // Exibe uma notificação ao usuário
            PopUp.showNotefy("Sucesso!!! Usuário criado, seja bem vindo(a).");

        } catch (SQLException error) {
            // Caso gere um erro
            PopUp.showWarning("DatabaseModel\\createUser\n" + error);

        } finally {
            // Finaliza toda a conexão com o banco de dados
            closeDb();
        }
    }

    // @author Kaik D' Andrade
    public void readUser(String userEmail, String userPassword) {
        
        // Comando SQL
        sql = "SELECT * FROM user WHERE u_email = ? AND u_password = sha2(?, 512)";

        try {
            // Conecta ao banco de dados, depois prepara, filtra e sanitiza o sql para exetuta-lo
            conectDb();
            setPstm(getConn().prepareStatement(sql));

            // Altera os "?" pelos valores corretos
            getPstm().setString(1, userEmail);
            getPstm().setString(2, userPassword);

            // Executando o comando SQL no banco de dados
            setRes(pstm.executeQuery());

            if (getRes().next()) {
                /**
                 * Se for True....
                 */
            } else {
                // Exibe uma mensagem de alerta ao usuário
                PopUp.showAlert("Email e/ou senha incorreto(s).\nVerifique os dados e tente novamente...");

                // Depois colocar aqui um método para limpar o campo de senha
                /**
                 *
                 */
            }
        } catch (SQLException error) {
            // Caso gere um erro
            PopUp.showWarning("DatabaseModel\\readUser\n" + error);

        } finally {
            // Finaliza toda a conexão com o banco de dados
            closeDb();
        }
    }

    // @author Gabriel Souza
    public void updateUser(int userId, String userName, String userEmail) {

        // Comando SQL
        sql = "UPDATE user SET u_name = ?, u_email = ? WHERE u_id = ?";

        try {
            // Conecta ao banco de dados, depois prepara, filtra e sanitiza o sql para executa-lo
            conectDb();
            setPstm(getConn().prepareStatement(sql));

            // Alterando os "?" pelos valores corretos
            getPstm().setString(1, userName);
            getPstm().setString(2, userEmail);
            getPstm().setInt(3, userId);

            // Executa o comando SQL no banco de dados
            getPstm().execute();

            // Exibe uma notificação ao usuário
            PopUp.showNotefy("Sucesso!!! Dados alterados.");

        } catch (SQLException error) {
            // Caso gere um erro
            PopUp.showWarning("DatabaseModel\\updateUser\n" + error);

        } finally {
            // Finaliza a toda a conexão com o banco de dados
            closeDb();
        }
    }
    
    // @author Kaik D' Andrade
    public void setPassword(String oldPass, String newPass) {

        // Comando SQL
        sql = "SELECT u_password FROM user WHERE u_password = sha2(?, 512)";


        try {
            // Conecta ao banco de dados, depois prepara, filtra e sanitiza o sql para exetuta-lo
            conectDb();
            setPstm(getConn().prepareStatement(sql));

            //getPstm().setString(1, "sha2("+ oldPass +", 512)");
            //getPstm().setInt(2, 512);
            getPstm().setInt(1, Integer.parseInt(oldPass));
            
            // Armazenando o resultado e jogando ele na variável userPass
            setRes(getPstm().executeQuery());
            getRes().next();
            String userPass = getRes().getString("u_passwd");
            System.out.println(userPass);

//            // Verificando se a senha antiga passada como parâmetro está correta
//            if (userPass.equals(oldPass)) {
//                // Comando SQL
//                sql = "UPDATE usuario SET u_passwd = ? WHERE u_id = ?";
//
//                // Prepara, filtra e sanitiza o sql e depois troca os "?" pelos valores corretos e executa o comando SQL no banco
//                setPstm(getConn().prepareStatement(sql));
//                getPstm().setString(1, newPass);
//                getPstm().setInt(2, userId);
//                // executa o comando no banco de dados assim trocando a senha do usuário pela nova senha passada como parâmetro
//                getPstm().execute();
//
//                PopUp.showNotefy("Sucesso!!! Sua senha foi alterada.");
//            } else {
//                // exibe uma mensagem de alerta ao usuário
//                PopUp.showAlert("Senha incorreta, tente novamente...");
//
//                // Depois colocar aqui um método para limpar os campos
//                /**
//                 * 
//                 */
//            }

            // Altera os "?" pelos valores corretos
            getPstm().setString(1, oldPass);

            // Executando o comando SQL no banco de dados
            setRes(pstm.executeQuery());

            if (getRes().next()) {
                // Comando SQL
                sql = "UPDATE user SET u_password = ? WHERE u_password = sha2(?, 512)";

                // Prepara, filtra e sanitiza o sql
                setPstm(getConn().prepareStatement(sql));

                // Altera os "?" pelos valores corretos
                getPstm().setString(1, newPass);
                getPstm().setString(2, oldPass);

                // executa o comando no banco de dados
                getPstm().execute();

                // Exibe uma notificação ao usuário
                PopUp.showNotefy("Sucesso!!! Sua senha foi alterada.");
            } else {
                // Exibe uma mensagem de alerta ao usuário
                PopUp.showAlert("Senha incorreta, tente novamente...");

                // Depois colocar aqui um método para limpar os campos
                /**
                 *
                 */
            }

        } catch (SQLException error) {
            // Caso gere um erro
            PopUp.showWarning("DatabaseModel\\setPassword\n" + error);

        } finally {
            // Finaliza toda a conexão com o banco de dados
            closeDb();
        }
    }
    

    // @author Gabriel Souza
    public void deleteUser(int userId) {

        // Comando SQL
        sql = "UPDATE user SET u_status = 'del' WHERE u_id = ?";

        try {
            // Conecta ao banco de dados, depois prepara, filtra e sanitiza o sql para executa-lo
            conectDb();
            setPstm(getConn().prepareStatement(sql));

            // Altera os "?" pelos valores corretos
            getPstm().setInt(1, userId);

            // Executa o comando SQL no banco de dados
            getPstm().execute();

        } catch (SQLException error) {
            // Caso gere um erro
            PopUp.showWarning("DatabaseModel\\deleteUser\n" + error);

        } finally {
            // Finaliza toda a conexão com o banco de dados
            closeDb();
        }
    }

    // @author Kaik D' Andrade
    public void createArtist(String artistName) {

        // Comando SQL
        sql = "INSERT INTO artist(u_name) VALUES (?)";

        try {
            // Conecta ao banco de dados, depois prepara, filtra e sanitiza o sql para executa-lo
            conectDb();
            setPstm(getConn().prepareStatement(sql));

            // Alterando os "?" pelos valores corretos
            getPstm().setString(1, artistName);

            // Executa o comando SQL no banco de dados
            getPstm().execute();

            // Exibe uma notificação ao usuário
            PopUp.showNotefy("Sucesso!!! Novo artista cadastrado.");

        } catch (SQLException error) {
            // Caso gere um erro
            PopUp.showWarning(error);

        } finally {
            // Finaliza toda a conexão com o banco de dados
            closeDb();
        }
    }
    
    /*
        Read
        update
        delete
    */
    
        public void updateArtist(String artistName, int artistId) {

        // Comando SQL
        sql = "UPDATE artist SET a_name = ? WHERE a_id = ?";

        try {
            // Conecta ao banco de dados, depois prepara, filtra e sanitiza o sql para executa-lo
            conectDb();
            setPstm(getConn().prepareStatement(sql));

            // Alterando os "?" pelos valores corretos
            getPstm().setString(1, artistName);
            getPstm().setInt(2, artistId);

            // Executa o comando SQL no banco de dados
            getPstm().execute();

            // Exibe uma notificação ao artista
            PopUp.showNotefy("Sucesso!!! Artista editado.");

        } catch (SQLException error) {
            // Caso gere um erro
            PopUp.showWarning(error);

        } finally {
            // Finaliza toda a conexão com o banco de dados
            closeDb();
        }
    }
        
    public void deleteArtist(int artistId) {

        // Comando SQL
        sql = "DELETE FROM artist WHERE a_id = ?";

        try {
            // Conecta ao banco de dados, depois prepara, filtra e sanitiza o sql para executa-lo
            conectDb();
            setPstm(getConn().prepareStatement(sql));

            // Altera os "?" pelos valores corretos
            getPstm().setInt(1, artistId);

            // Executa o comando SQL no banco de dados
            getPstm().execute();
            
            // Exibe uma notificação ao artista
            PopUp.showNotefy("Sucesso!!! Artista deletado.");

        } catch (SQLException error) {
            // Caso gere um erro
            PopUp.showWarning(error);

        } finally {
            // Finaliza toda a conexão com o banco de dados
            closeDb();
        }
    
    }
        

//        public void insertMusic(Music music) throws FileNotFoundException {
//
//        String sql = "INSERT INTO music ( m_name, m_duration, m_banner, m_music) VALUES ( ?, ?, ?, ? )";
//
//        try {
//
//            PreparedStatement stmt = this.conn.prepareStatement(sql);
//            stmt.setString(1, music.getName());
//            stmt.setInt(2, music.getDuration());
//            File image = new File("imagem.jpg");
//            fis = new FileInputStream(image);
//            stmt.setBinaryStream(3, fis, (int) image.length());
//            File audio = new File("musica.mp3");
//            fis = new FileInputStream(audio);
//            stmt.setBinaryStream(3, fis, (int) audio.length());
//            stmt.execute();
//
//        } catch (SQLException | FileNotFoundException erro) {
//
//            System.out.println("ERROR : " + erro.getMessage());
//
//        } finally {
//
//            dbClose(conn, null, null);
//
//        }
//
//    }
//
//    public void editMusic(Music music) {
//
//        String sql = "UPDATE music SET  m_name = ?, m_duration = ?, m_banner = ?, m_music = ? WHERE id = ?";
//
//        try {
//
//            PreparedStatement stmt = this.conn.prepareStatement(sql);
//            stmt.setString(1, music.getName());
//            stmt.setInt(2, music.getDuration());
//            File image = new File("imagem.jpg");
//            fis = new FileInputStream(image);
//            stmt.setBinaryStream(3, fis, (int) image.length());
//            File audio = new File("musica.mp3");
//            fis = new FileInputStream(audio);
//            stmt.setBinaryStream(3, fis, (int) audio.length());
//            stmt.execute();
//
//        } catch (SQLException | FileNotFoundException erro) {
//
//            System.out.println("ERROR : " + erro.getMessage());
//
//        } finally {
//
//            dbClose(conn, null, null);
//
//        }
//
//    }
//
//
//
//    public void deleteMusic(int m_id) {
//
//        String sql = "DELETE FROM music WHERE m_id = ?";
//
//        try {
//
//            PreparedStatement stmt = this.conn.prepareStatement(sql);
//            stmt.setInt(1, m_id);
//            stmt.execute();
//
//        } catch (SQLException erro) {
//
//            System.out.println("ERROR : " + erro.getMessage());
//
//        }
//
//    }
    /**
     * @return the conn
     */
    public Connection getConn() {
        return conn;
    }

    /**
     * @param conn the conn to set
     */
    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     * @return the pstm
     */
    public PreparedStatement getPstm() {
        return pstm;
    }

    /**
     * @param pstm the pstm to set
     */
    public void setPstm(PreparedStatement pstm) {
        this.pstm = pstm;
    }

    /**
     * @return the res
     */
    public ResultSet getRes() {
        return res;
    }

    /**
     * @param res the res to set
     */
    public void setRes(ResultSet res) {
        this.res = res;
    }
}
