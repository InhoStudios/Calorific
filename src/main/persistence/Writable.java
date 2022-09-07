package persistence;

import org.json.JSONObject;

/**
 * Class adapted from the Writable interface in
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 *
 * Models an object that can be written as a json
 *
 */
public interface Writable {
    // EFFECTS: returns object as a JSONObject
    JSONObject toJson();
}
