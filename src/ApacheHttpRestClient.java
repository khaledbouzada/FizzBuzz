

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedInputStream;
import java.io.InputStream;


public class ApacheHttpRestClient {

    public final static void main(String[] args) {

        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpGet request = null;
            HttpPost post = null;
            HttpPut put = null;
            if (args.length == 0) {
                System.out.println("----------------------------------------");
                System.out.println("Please enter a request method and url!");
                System.out.println("java -jar FizzBuzz.jar 'get' 'http://localhost:3000/users/1.json'");
                System.out.println("java -jar FizzBuzz.jar 'post' 'http://localhost:3000/users.json' \"user[first_name]=sam&user[last_name]=philips\"");
                System.out.println("java -jar FizzBuzz.jar 'put' 'http://localhost:3000/users/2.json?number=88'");
                System.out.println("----------------------------------------");
            } else {
                if (args[0].equals("get")) {
                    request = new HttpGet(args[1]);
                } else if (args[0].equals("post")) {
                    post = new HttpPost(args[1]);
                    StringEntity params = new StringEntity(args[2]);
                    post.addHeader("content-type", "application/x-www-form-urlencoded");
                    post.setEntity(params);
                } else if (args[0].equals("put")) {
                    put = new HttpPut(args[1]);
                } else {
                    System.out.println("----------------------------------------");
                    System.out.println("invalid http verb!");
                    System.out.println("----------------------------------------");
                }

                HttpResponse httpResponse = request == null ? (put == null ? httpClient.execute(post) : httpClient.execute(put)) : httpClient.execute(request);

                System.out.println("----------------------------------------");
                System.out.println(httpResponse.getStatusLine());
                System.out.println("----------------------------------------");

                HttpEntity entity = httpResponse.getEntity();

                byte[] buffer = new byte[1024];
                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    try {
                        int bytesRead = 0;
                        BufferedInputStream bis = new BufferedInputStream(inputStream);
                        while ((bytesRead = bis.read(buffer)) != -1) {
                            String chunk = new String(buffer, 0, bytesRead);
                            System.out.println(chunk);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            inputStream.close();
                        } catch (Exception ignore) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
}