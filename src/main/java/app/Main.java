package app;

import app.config.ThymeleafConfig;
import app.controllers.UserController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "fourthingsplus";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);



    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            JavalinThymeleaf.init(ThymeleafConfig.templateEngine());
        }).start(7070);



        // Routing

        /*
        .get benyttes når der skal "hentes" noget.
        .post benyttes når der skal
         */

        //Når man besøger hjemmesiden skal index siden renderes.
        app.get("/", ctx -> ctx.render("index.html"));
        //Når man indtaster sine oplysninger skal der logges ind
        app.post("/login", ctx -> UserController.login(ctx,connectionPool));
        //Når der klikkes på linket "Create user" skal undersiden createuser renderes.
        app.get("/createuser", ctx -> ctx.render("createuser.html"));
        // Når der klikkes på createuser, følges denne rute:
        app.post("/createuser", ctx -> UserController.createUser(ctx, connectionPool));
        //Når jeg ønsker at tilføje en task skal der benyttes post da jeg ønsker at tilføje noget til tabellen
        app.post("/addtask", ctx -> UserController.addTask(ctx, connectionPool));
        app.post("/delete", ctx -> UserController.deleteTask(ctx, connectionPool));
        app.post("/done", ctx -> UserController.taskDone(ctx, connectionPool));
        app.post("/undo", ctx -> UserController.taskUndo(ctx, connectionPool));
        //Når man er inde på tiden tasks og klikker på edit, føres man til undersiden edittasks hvor man kan ændre task title
        app.post("/edit", ctx -> UserController.getTask(ctx, connectionPool));
        app.post("/updatetask", ctx -> UserController.editTask(ctx, connectionPool));
        app.post("/usertasks", ctx -> UserController.getAllTasks(ctx,connectionPool));
        app.get("/logout", ctx -> UserController.logout(ctx, connectionPool));





        //app.post("/task", ctx -> UserController.editTask(ctx, connectionPool));


    }
}