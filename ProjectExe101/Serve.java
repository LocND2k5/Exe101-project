import com.sun.net.httpserver.*;import java.io.*;import java.net.*;import java.nio.file.*;
public class Serve{public static void main(String[] a)throws Exception{Path root=Paths.get(a[0]).toAbsolutePath().normalize();int port=Integer.parseInt(a[1]);
HttpServer s=HttpServer.create(new InetSocketAddress("127.0.0.1",port),0);
s.createContext("/",ex->{try{String p=URLDecoder.decode(ex.getRequestURI().getPath(),"UTF-8");if(p.equals("/"))p="/index.html";Path f=root.resolve(p.substring(1)).normalize();
if(!f.startsWith(root)||!Files.isRegularFile(f)){ex.sendResponseHeaders(404,-1);ex.close();return;}
String n=f.toString().toLowerCase();String ct=n.endsWith(".html")?"text/html; charset=utf-8":n.endsWith(".mp4")?"video/mp4":n.endsWith(".js")?"text/javascript":n.endsWith(".css")?"text/css":n.endsWith(".svg")?"image/svg+xml":n.endsWith(".png")?"image/png":n.endsWith(".webp")?"image/webp":"application/octet-stream";
long len=Files.size(f);ex.getResponseHeaders().set("Content-Type",ct);ex.getResponseHeaders().set("Accept-Ranges","bytes");ex.getResponseHeaders().set("Cache-Control","no-store");
String r=ex.getRequestHeaders().getFirst("Range");long st=0,en=len-1;int code=200;
if(r!=null&&r.startsWith("bytes=")){String[] q=r.substring(6).split("-",-1);st=Long.parseLong(q[0]);if(q.length>1&&!q[1].isEmpty())en=Long.parseLong(q[1]);code=206;ex.getResponseHeaders().set("Content-Range","bytes "+st+"-"+en+"/"+len);}
long cl=en-st+1;ex.sendResponseHeaders(code,cl);try(RandomAccessFile raf=new RandomAccessFile(f.toFile(),"r");OutputStream o=ex.getResponseBody()){raf.seek(st);byte[] b=new byte[65536];long left=cl;int k;while(left>0&&(k=raf.read(b,0,(int)Math.min(b.length,left)))>0){o.write(b,0,k);left-=k;}}}catch(IOException e){}finally{ex.close();}});
s.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(8));s.start();System.out.println("on "+port);}}
