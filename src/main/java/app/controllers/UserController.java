package app.controllers;

import app.entities.Task;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.TaskMapper;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.util.List;

public class UserController {

    public static void login(Context ctx, ConnectionPool connectionPool){
        String name = ctx.formParam("username");
        String password = ctx.formParam("password");

        try {
            User user = UserMapper.login(name, password, connectionPool);
            ctx.sessionAttribute("currentUser", user); // Således kan vi altid hive fat i brugeren. Tjek task siden

            //get tasks belonging to this user
            List<Task> listOfTasks = TaskMapper.getAllTasksPerUser(user.getId(), connectionPool);

            //I attribute gemmer vi et listen over tasks i varibale "tasks"
            ctx.attribute("tasks", listOfTasks);
            ctx.render("tasks.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }

    }

    public static void createUser(Context ctx, ConnectionPool connectionPool) {

        //her fisker man de oplysninger der er indtastet i formularen
        String name = ctx.formParam("username");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        //validering af passwords -  at de matcher
        if(password1.equals(password2)){
            try {
                UserMapper.createuser(name, password1, connectionPool);
                ctx.attribute("message", "Your account is created. You can now login");
                ctx.render("index.html");
            } catch (DatabaseException e) {
                ctx.attribute("message", e.getMessage());
                ctx.render("createuser.html");
            }
        }  else{
            ctx.attribute("message", "Your passwords are not identical");
            ctx.render("createuser.html");
        }
    }

    public static void addTask(Context ctx, ConnectionPool connectionPool){
        String name = ctx.formParam("item_name");
        User user = ctx.sessionAttribute("currentUser");

        try {
            TaskMapper.addTask(user.getId(), name, connectionPool);

            //get tasks belonging to this user
            List<Task> listOfTasks = TaskMapper.getAllTasksPerUser(user.getId(), connectionPool);

            //I attribute gemmer vi et listen over tasks i varibale "tasks"
            ctx.attribute("tasks", listOfTasks);
            ctx.render("tasks.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }


    public static void deleteTask(Context ctx, ConnectionPool connectionPool){
        int taskId = Integer.parseInt(ctx.formParam("task_id"));
        User user = ctx.sessionAttribute("currentUser");

        try {
            TaskMapper.deleteTask(taskId,connectionPool);

            //get tasks belonging to this user
            List<Task> listOfTasks = TaskMapper.getAllTasksPerUser(user.getId(), connectionPool);

            //I attribute gemmer vi et listen over tasks i varibale "tasks"
            ctx.attribute("tasks", listOfTasks);
            ctx.render("tasks.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    public static void taskDone(Context ctx, ConnectionPool connectionPool){
        int taskId = Integer.parseInt(ctx.formParam("task_id"));
        User user = ctx.sessionAttribute("currentUser");

        try {
            TaskMapper.taskDone(taskId,connectionPool);

            //get tasks belonging to this user
            List<Task> listOfTasks = TaskMapper.getAllTasksPerUser(user.getId(), connectionPool);

            //I attribute gemmer vi et listen over tasks i varibale "tasks"
            ctx.attribute("tasks", listOfTasks);
            ctx.render("tasks.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    public static void taskUndo(Context ctx, ConnectionPool connectionPool){
        int taskId = Integer.parseInt(ctx.formParam("task_id"));
        User user = ctx.sessionAttribute("currentUser");

        try {
            TaskMapper.taskUndo(taskId,connectionPool);

            //get tasks belonging to this user
            List<Task> listOfTasks = TaskMapper.getAllTasksPerUser(user.getId(), connectionPool);

            //I attribute gemmer vi et listen over tasks i varibale "tasks"
            ctx.attribute("tasks", listOfTasks);
            //ctx.attribute("tasksDone", tasksDone);
            ctx.render("tasks.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    public static void getTask(Context ctx, ConnectionPool connectionPool) {

        int taskId = Integer.parseInt(ctx.formParam("task_id"));
        User user = ctx.sessionAttribute("currentUser");

        try {

            //get task to edit belonging to this user
            Task task = TaskMapper.getTask(taskId, connectionPool);

            //I attribute gemmer vi et listen over tasks i varibale "tasks"
            ctx.attribute("task", task);
            //ctx.attribute("tasksDone", tasksDone);
            ctx.render("edittasks.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }

    }


    public static void editTask(Context ctx, ConnectionPool connectionPool) {
        int taskId = Integer.parseInt(ctx.formParam("task_id"));
        String newTitle = ctx.formParam("task_name");
        User user = ctx.sessionAttribute("currentUser");

        try {
            TaskMapper.editTask(taskId, newTitle, connectionPool);

            //get tasks belonging to this user
            List<Task> listOfTasks = TaskMapper.getAllTasksPerUser(user.getId(), connectionPool);

            //I attribute gemmer vi et listen over tasks i varibale "tasks"
            ctx.attribute("tasks", listOfTasks);
            //ctx.attribute("tasksDone", tasksDone);
            ctx.render("tasks.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    public static void getAllTasks(Context ctx, ConnectionPool connectionPool) {
        User user = ctx.sessionAttribute("currentUser");

        try {
            TaskMapper.getAllTasksPerUser(user.getId(),connectionPool);

            //get tasks belonging to this user
            List<Task> listOfTasks = TaskMapper.getAllTasksPerUser(user.getId(), connectionPool);

            //I attribute gemmer vi et listen over tasks i varibale "tasks"
            ctx.attribute("tasks", listOfTasks);
            ctx.render("tasks.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }
    }

    public static void logout(Context ctx, ConnectionPool connectionPool) {

        ctx.req().getSession().invalidate();
        ctx.redirect("index.html");

    }
}
