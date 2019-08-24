package softagi.androidworkshop1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import softagi.androidworkshop1.Models.BookModel;

public class Utils
{
    private static String title,author,image;

    public static List<BookModel> utils(String api)
    {
        URL url = createURL(api);
        String json = makeHTTPrequest(url);
        List<BookModel> b = extractDatafromJSON(json);

        return b;
    }

    private static URL createURL (String api)
    {
        URL url = null;

        try
        {
            url = new URL(api);
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        return url;
    }

    public static String makeHTTPrequest (URL url)
    {
        String response = "";

        if (url == null)
        {
            return response;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try
        {
            httpURLConnection = (HttpURLConnection)  url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200)
            {
                inputStream = httpURLConnection.getInputStream();
                response = readFromStream(inputStream);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (httpURLConnection != null)
            {
                httpURLConnection.disconnect();
            }

            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    private static String readFromStream (InputStream inputStream)
    {
        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("utf-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            try
            {
                String line = bufferedReader.readLine();

                while (line != null)
                {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    public static List<BookModel> extractDatafromJSON (String json)
    {
        if (json == null)
        {
            return null;
        }

        List<BookModel> aa = new ArrayList<>();

        try
        {
            JSONObject root = new JSONObject(json);

            JSONArray items = root.getJSONArray("items");

            for (int i = 0 ; i < items.length() ; i ++)
            {
                JSONObject each_item = items.getJSONObject(i);
                JSONObject vi = each_item.getJSONObject("volumeInfo");

                if (vi.has("title"))
                {
                    title = vi.getString("title");
                } else
                    {
                        title = "Not found";
                    }

                if (vi.has("authors"))
                {
                    author = vi.getJSONArray("authors").getString(0);
                } else
                {
                    author = "Not found";
                }

                if (vi.has("imageLinks"))
                {
                    image = vi.getJSONObject("imageLinks").getString("thumbnail");
                } else
                {
                    image = "";
                }

                BookModel bookModel = new BookModel(title,author,image);
                aa.add(bookModel);
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return aa;
    }
}
