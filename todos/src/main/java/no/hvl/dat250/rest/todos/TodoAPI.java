package no.hvl.dat250.rest.todos;

import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.util.Objects.requireNonNull;
import static spark.Spark.*;

/**
 * Rest-Endpoint.
 */
public class TodoAPI {

    static Todo todo;

    public static void main(String[] args) {
        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        Random random = new Random();

        Map<Long, Todo> todos = new HashMap<>();

        todo = new Todo(random.nextLong(), "generic summary", "generic description");

        after((req, res) -> res.type("application/json"));
        Gson gson = new Gson();

        get("/todos", (req, res) -> gson.toJson(todos.values()));

        get("/todos/:id", (req, res) -> {
            String ids = req.params("id");
            long id;
            try{
                id = Long.parseLong(ids);
            }catch (NumberFormatException e){
                return "The id \"" + ids + "\" is not a number!";
            }

            Todo result = todos.get(id);
            if (result == null){
                return "Todo with the id  \"" + id + "\" not found!";
            }
            return gson.toJson(result);
        });

        post("/todos", (req, res) -> {
                 todo = gson.fromJson(req.body(), Todo.class);
                 todos.put(todo.getId(), todo);
                 return gson.toJson(todo);
         });

        delete("/todos/:id", (req, res) -> {
            String ids = req.params("id");
            long id;
            try{
                id = Long.parseLong(ids);
            }catch (NumberFormatException e){
                return "The id \"" + ids + "\" is not a number!";
            }
            return todos.remove(id);
        });

        put("/todos/:id", (req, res)->{
            String ids = req.params("id");
            long id;
            try{
                id = Long.parseLong(ids);
            }catch (NumberFormatException e){
                return "The id \"" + ids + "\" is not a number!";
            }
            return todos.put(id, gson.fromJson(req.body(), Todo.class));

        });

    }
}
