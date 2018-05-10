import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class musicDB {
    private static final String url = "jdbc:sqlite:music.sqlite";
    private static final String TABLE_ALBUMS = "albums";
    private static final String COLUMN_ALBUM_ID = "_id";
    private static final String COLUMN_ALBUM_NAME = "name";
    private static final String COLUMN_ALBUM_ARTIST = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    private static final String TABLE_ARTISTS = "artists";
    private static final String COLUMN_ARTIST_ID = "_id";
    private static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    private static final String TABLE_SONGS = "songs";
    private static final String COLUMN_SONG_ID = "_id";
    private static final String COLUMN_SONG_TRACK = "track";
    private static final String COLUMN_SONG_TITLE = "title";
    private static final String COLUMN_SONG_ALBUM = "album";
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    //get all albums

    private static final String QUERY_ALL_ALBUMS =
            "SELECT " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_ID+", "+ TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME +", "+ TABLE_ARTISTS + '.' + COLUMN_ARTIST_NAME+ " FROM " + TABLE_ALBUMS +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
                    " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID;
    //get all songs
    private static final String QUERY_ALL_SONGS =
            "SELECT " + TABLE_SONGS + '.' + COLUMN_SONG_ID+", "+ TABLE_SONGS + '.' + COLUMN_SONG_TRACK +", "+ TABLE_SONGS + '.' +
                    COLUMN_SONG_TITLE+", "+ TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME+", "+ TABLE_ARTISTS + '.' + COLUMN_ARTIST_NAME+
                    " FROM " + TABLE_SONGS+" INNER JOIN "+TABLE_ALBUMS +
                    " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +" = " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM+
                    " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
                    " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID;
    //Insert a song using prepared statements
    private static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS +
            '(' + COLUMN_ARTIST_NAME + ") VALUES(?)";
    private static final String INSERT_ALBUMS = "INSERT INTO " + TABLE_ALBUMS +
            '(' + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST + ") VALUES(?, ?)";

    private static final String INSERT_SONGS = "INSERT INTO " + TABLE_SONGS +
            '(' + COLUMN_SONG_TRACK + ", " + COLUMN_SONG_TITLE + ", " + COLUMN_SONG_ALBUM +
            ") VALUES(?, ?, ?)";
    private static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID + " FROM " +
            TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";

    private static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " +
            TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?";

    //    delete a song
    public static final String DELETE_QUERY="DELETE FROM "+TABLE_SONGS+" WHERE "+TABLE_SONGS+'.'+COLUMN_SONG_ID+" = ?";
    //prepare statements for add a song
    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSongs;
    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbum;

    //    prepare statement for delete a song
    private PreparedStatement deleteSong;

    //   code didn't work without instantiation. WHY?
    private static musicDB instance=new musicDB();


    private Connection conn;
//        private static Statement statement;

//    public static void main(String[] args)throws SQLException {

    public musicDB() {
        setUp();
    }
    public static musicDB getInstance(){
        return instance;
    }
    //create the database
    private void setUp() {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            String createTableAlbumsTemplate = "CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s INTEGER)";
            String createTableAlbums = String.format(createTableAlbumsTemplate, TABLE_ALBUMS, COLUMN_ALBUM_ID,
                    COLUMN_ALBUM_NAME, COLUMN_ALBUM_ARTIST
            );
//            System.out.println(createTableAlbums);
            statement.executeUpdate(createTableAlbums);

            //
            String createTableArtistTemplate = "CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL)";
            String createTableArtist = String.format(createTableArtistTemplate, TABLE_ARTISTS, COLUMN_ARTIST_ID, COLUMN_ARTIST_NAME
            );
            statement.execute(createTableArtist);



            String createTableSongsTemplate = "CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT NOT NULL, %s INTEGER)";
            String createTableSongs = String.format(createTableSongsTemplate, TABLE_SONGS, COLUMN_SONG_ID, COLUMN_SONG_TRACK,
                    COLUMN_SONG_TITLE, COLUMN_SONG_ALBUM
            );

//        System.out.println(createTableSongs);
            statement.execute(createTableSongs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean open() {
        try {
            conn = DriverManager.getConnection(url);
//            insert the prepared statments to the open method to cereate the instances to insert a song
            insertIntoArtists = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums = conn.prepareStatement(INSERT_ALBUMS, Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = conn.prepareStatement(INSERT_SONGS);
            queryArtist = conn.prepareStatement(QUERY_ARTIST);
            queryAlbum = conn.prepareStatement(QUERY_ALBUM);
            deleteSong=conn.prepareStatement(DELETE_QUERY);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
//            insert a new song
            if (insertIntoArtists != null) {
                insertIntoArtists.close();
            }

            if (insertIntoAlbums != null) {
                insertIntoAlbums.close();
            }

            if (insertIntoSongs != null) {
                insertIntoSongs.close();
            }

            if (queryArtist != null) {
                queryArtist.close();
            }

            if (queryAlbum != null) {
                queryAlbum.close();
            }

            if (conn != null) {
                conn.close();
            }

        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }
    //    get album column names
    Vector getAlbumsColumnNames() {

        Vector colNames = new Vector();

        colNames.add("ID");

        colNames.add("Album Name");
        colNames.add("Artist Name");

        return colNames;

    }
//    get albums data

    Vector<Vector> queryAllAlbums() {

        try (Connection conn=DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(QUERY_ALL_ALBUMS);
            Vector<Vector> albums = new Vector<>();
            String albumName;
            String artistName;
            int id;
            while (results.next()) {
                Vector album = new Vector();
                id=results.getInt(1);
                albumName=results.getString(2);
                artistName=results.getString(3);
                album.add(id);album.add(albumName);album.add(artistName);
                albums.add(album);
            }

            return albums;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }

    }
    //    get Songs column names
    Vector getSongsColumnNames() {

        Vector colNames = new Vector();

        colNames.add("ID");
        colNames.add("Track#");
        colNames.add("Title");
        colNames.add("Album Name");
        colNames.add("Artist Name");

        return colNames;

    }
//    get all songs data

    Vector<Vector> queryAllSongs() {

        try (Connection conn=DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery(QUERY_ALL_SONGS);
            Vector<Vector> songs = new Vector<>();
            String title;
            String albumNameSongs;
            String artistNameSongs;
            int id;
            int track;
            while (results.next()) {
                Vector song = new Vector();
                id=results.getInt(1);
                track=results.getInt(2);
                title=results.getString(3);
                albumNameSongs=results.getString(4);
                artistNameSongs=results.getString(5);
                song.add(id); song.add(track);song.add(title);song.add(albumNameSongs);song.add(artistNameSongs);
                songs.add(song);
            }

            return songs;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }

    }
    //    get artist column names
    Vector getArtistColumnNames() {

        Vector colNames = new Vector();

        colNames.add("ID");

        colNames.add("Name");

        return colNames;

    }
    Vector<Vector> queryAllArtists() {

        try (Connection conn=DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_ARTISTS);
            Vector<Vector> artists = new Vector<>();
            String name;
            int id;
            while (results.next()) {
                Vector artist = new Vector();
                id=results.getInt(INDEX_ARTIST_ID);
                name=results.getString(INDEX_ARTIST_NAME);
                artist.add(id);artist.add(name);
                artists.add(artist);
            }

            return artists;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }

    }

//
//    public List<String> queryAlbumsForArtist(String artistName) {
////        SELECT albums.name from albums
////        INNER JOIN artists
////        ON albums.artist=artists._id
////        WHERE artists.name=artistName
//        StringBuilder sb = new StringBuilder("SELECT ");
//        sb.append(TABLE_ALBUMS);
//        sb.append('.');
//        sb.append(COLUMN_ALBUM_NAME);
//        sb.append(" FROM ");
//        sb.append(TABLE_ALBUMS);
//        sb.append(" INNER JOIN ");
//        sb.append(TABLE_ARTISTS);
//        sb.append(" ON ");
//        sb.append(TABLE_ALBUMS);
//        sb.append('.');
//        sb.append(COLUMN_ALBUM_ARTIST);
//        sb.append(" = ");
//        sb.append(TABLE_ARTISTS);
//        sb.append('.');
//        sb.append(COLUMN_ARTIST_ID);
//        sb.append(" WHERE ");
//        sb.append(TABLE_ARTISTS);
//        sb.append('.');
//        sb.append(COLUMN_ARTIST_NAME);
//        sb.append(" = \"");
//        sb.append(artistName);
//        sb.append("\"");
//        try (Statement statement = conn.createStatement();
//             ResultSet results = statement.executeQuery(sb.toString())) {
//            List<String> albums = new ArrayList<>();
//            while (results.next()) {
//                albums.add(results.getString(1));
//            }
//            return albums;
//        } catch (SQLException e) {
//            System.out.println("Query failed" + e.getMessage());
//            return null;
//        }
//    }

    //    insert artist(part of insert a song)
    private int insertArtist(String name) throws SQLException {
        queryArtist.setString(1, name);
        ResultSet results = queryArtist.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            //insert the artist
            insertIntoArtists.setString(1, name);
            int affectedRows = insertIntoArtists.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert artist");
            }
            ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get artist's id");
            }
        }
    }

    //    insert album(part of insert a song)
    private int insertAlbum(String name, int artistId) throws SQLException {
        queryAlbum.setString(1, name);
        ResultSet results = queryAlbum.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            //insert tye album
            insertIntoAlbums.setString(1, name);
            insertIntoAlbums.setInt(2, artistId);
            int affectedRows = insertIntoAlbums.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert album");
            }
            ResultSet generatedKeys = insertIntoAlbums.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get artist's album");
            }
        }
    }

    //    insert song(part of insert a song)
    public void insertSong(String title, String artist, String album, int track) {

        try {
//            set the autocommit to false to execute the entire code as a one a transaction
            conn.setAutoCommit(false);
//add the artist name if new
            int artistId = insertArtist(artist);
//            add the album name if new
            int albumId = insertAlbum(album, artistId);
            insertIntoSongs.setInt(1, track);
            insertIntoSongs.setString(2, title);
            insertIntoSongs.setInt(3, albumId);
            int affectedRows = insertIntoSongs.executeUpdate();
            if(affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("The song insert failed");
            }

        } catch(SQLException e) {
            System.out.println("Insert song exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch(SQLException e2) {
                System.out.println("What did you do?!! " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
    }
    public void deleteSong(int SongID) {

        try {

            deleteSong.setInt(1, SongID);

            deleteSong.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(e);

        }

    }
}