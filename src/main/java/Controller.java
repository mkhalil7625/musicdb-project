import java.util.ArrayList;
import java.util.List;

public class Controller {
//    private musicDB db;
//    private musicGUI gui;

    public static void main(String[] args) {

        musicDB datasource = new musicDB();
        if(!datasource.open()) {
            System.out.println("Can't open datasource");
            return;
        }


//
//        List<Artist> artists = datasource.queryArtists();
//        if (artists == null) {
//            System.out.println("No artists!");
//            return;
//        }
//        for (Artist artist : artists) {
//            System.out.println("ID: " + artist.getID() + ", Name= " + artist.getName());
//
//        }
////        call queryAlbumsForArtist
//        List<String> albumsForArtist= datasource.queryAlbumsForArtist("Iron Maiden");
//                for(String ar:albumsForArtist){
//                    System.out.println(ar);
//                }
//                datasource.insertSong("sway", "dean martin","sway" , 1);
        musicGUI gui=new musicGUI(datasource);
//        datasource.close();
    }

}
