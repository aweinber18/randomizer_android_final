package com.example.randomizer.model;

import com.google.gson.Gson;

public class RandomNumber
{
    private int from, to, currentRandomNumber;

    public RandomNumber ()
    {
        setFrom (0);
        setTo (1);
    }

    public RandomNumber (int numberOfValues)
    {
        setFrom (1);
        setTo (numberOfValues);
    }

    public RandomNumber (int from, int to)
    {
        this();
        setCurrentRandomNumberToNewlyGenerated ();
    }

    private int generateRandomNumber ()
    {
        double rand1 = Math.random ();
        double rand2 = rand1 * (1 + (to - from));
        double rand3 = Math.floor (rand2);
        return (int) rand3 + from;
    }

    public final void setCurrentRandomNumberToNewlyGenerated ()
    {
        currentRandomNumber = generateRandomNumber ();
    }

    public int getCurrentRandomNumber ()
    {
        return currentRandomNumber;
    }

    public int getFrom ()
    {
        return from;
    }

    public void setFromTo(int from, int to)
    {
        setFrom (from);
        setTo (to);
        setCurrentRandomNumberToNewlyGenerated ();
    }

    private void setFrom (int from)
    {
        this.from = from;
    }

    public int getTo ()
    {
        return to;
    }

    private void setTo (int to)
    {
        if (to <= from) {
            throw new RuntimeException ("To must be greater than From.");
        }

        this.to = to;
    }

    /**
     * Reverses the game object's serialization as a String
     * back to a ThirteenStones game object
     *
     * @param json The serialized String of the game object
     * @return The game object
     */
    public static RandomNumber getGameFromJSON (String json)
    {
        Gson gson = new Gson ();
        return gson.fromJson (json, RandomNumber.class);
    }

    /**
     * Serializes the game object to a JSON-formatted String
     *
     * @param obj Game Object to serialize
     * @return JSON-formatted String
     */
    public static String getJSONFromGame (RandomNumber obj)
    {
        Gson gson = new Gson ();
        return gson.toJson (obj);
    }

    public String getJSONFromCurrentGame()
    {
        return getJSONFromGame(this);
    }
}
