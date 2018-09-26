package web;

import constants.paths.Path;
import db.DBManager;
import db.entity.User;
import exception.DBException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@WebServlet("/avatar")
public class AvatarUploader extends HttpServlet {

    private Random random = new Random();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processFile(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void processFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //проверяем является ли полученный запрос multipart/form-data
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Создаём класс фабрику
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Максимальный буфера данных в байтах,
        // при его привышении данные начнут записываться на диск во временную директорию
        // устанавливаем один мегабайт
        factory.setSizeThreshold(1024 * 1024);

        // устанавливаем временную директорию
        File tempDir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(tempDir);

        //Создаём сам загрузчик
        ServletFileUpload upload = new ServletFileUpload(factory);

        //максимальный размер данных который разрешено загружать в байтах
        //по умолчанию -1, без ограничений. Устанавливаем 10 мегабайт.
        upload.setSizeMax(1024 * 1024 * 10);
        HttpSession session = request.getSession();
        String requestString = (String) session.getAttribute("avatar");
        //String oldAvatar = requestString.substring(19, requestString.length());
        String[] array = null;
        String oldAvatar = null;
        if(requestString != null){
            array = requestString.split("/");
            oldAvatar = array[array.length - 1];
        }


        System.out.println(oldAvatar);
        String link = null;


        List items = upload.parseRequest(request);
        Iterator iter = items.iterator();

        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();

            if (item.isFormField()) {
                //если принимаемая часть данных является полем формы
                processFormField(item);
            } else {
                //в противном случае рассматриваем как файл
                File uploadedFile = null;
                //выбираем файлу имя пока не найдём свободное
                do {
                    String path = "D://travel_agency19_01/web/avatars/" + random.nextInt() + item.getName();
                    String avatarName = path.substring(34, path.length());
                    link = "../../../avatars" + avatarName;
                    //session.setAttribute("avatar", link);
                        /*request.setAttribute();*/

                    uploadedFile = new File(path);
                } while (uploadedFile.exists());

                //создаём файл
                uploadedFile.createNewFile();
                //записываем в него данные
                item.write(uploadedFile);
            }

        }

        User user = (User) session.getAttribute("user");
        DBManager dbManager = DBManager.getInstance();

        if(dbManager.checkIfAvatarExists(user.getId())){
            dbManager.insertAvatarPathById(user.getId(), link);
        } else {
            dbManager.updateAvatarLinkById(user.getId(), link);
        }


        Files.deleteIfExists(Paths.get("D://travel_agency19_01/web/avatars/" + oldAvatar));

        response.sendRedirect(Path.COMMAND_REDIRECT_PERSONAL_SETTINGS);
    }

    /**
     * Сохраняет файл на сервере, в папке upload.
     * Сама папка должна быть уже создана.
     *
     * @param item
     * @throws Exception
     */
    private void processUploadedFile(FileItem item) throws Exception {

    }

    /**
     * Выводит на консоль имя параметра и значение
     *
     * @param item
     */
    private void processFormField(FileItem item) {
        System.out.println(item.getFieldName() + "=" + item.getString());
    }
}
