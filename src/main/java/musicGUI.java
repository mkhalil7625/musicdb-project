import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class musicGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel displayPanel;
    private JButton showAllSongsButton;
    private JButton showAllAlbumsButton;
    private JButton showAllArtistsButton;
    private JTable displayTable;

    private JPanel dataEntryPanel;
    private JButton addSongButton;
    private JTextField titleTextField;
    private JTextField trackTextField;
    private JTextField albumTextField;
    private JTextField artistNameTextField;

    private DefaultTableModel tableModel;

    private Vector columnNames;

    private musicDB db;

    public musicGUI(musicDB db) {
        this.db=db;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setContentPane(mainPanel);

        pack();

        setVisible(true);
        //Set up JTable

        displayTable.setGridColor(Color.BLACK);

       //Enable sorting

        displayTable.setAutoCreateRowSorter(true);

        showAllArtistsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                columnNames = db.getArtistColumnNames();
                Vector data = db.queryAllArtists();
                // Custom methods for DefaultTableModel
                tableModel = new DefaultTableModel( data, columnNames);
                updateArtistTable();
                displayTable.setModel(tableModel);

            }
        });
        showAllAlbumsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                columnNames = db.getAlbumsColumnNames();
                Vector data = db.queryAllAlbums();
                // Custom methods for DefaultTableModel
                tableModel = new DefaultTableModel( data, columnNames);
                updateAlbumsTable();
                displayTable.setModel(tableModel);
            }
        });
        showAllSongsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                columnNames = db.getSongsColumnNames();
                Vector data = db.queryAllSongs();
               // Custom methods for DefaultTableModel
                tableModel = new DefaultTableModel( data, columnNames);
                updateSongsTable();
                displayTable.setModel(tableModel);
            }
        });
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSong();
            }
        });
    }

    private void addSong() {
        String songTitle=titleTextField.getText().toUpperCase();
        String albumName=albumTextField.getText().toUpperCase();
        String artistName=artistNameTextField.getText().toUpperCase();
        String textTrackNumber=trackTextField.getText();
        if (songTitle == null || songTitle.trim().equals("")) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter record info");
            return;
        }
        if (albumName == null || albumName.trim().equals("")) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter record info");
            return;
        }        if (artistName == null || artistName.trim().equals("")) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter record info");
            return;
        }        if (textTrackNumber == null || textTrackNumber.trim().equals("")) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter record info");
            return;
        }
        int trackNumber;
        try {
            trackNumber=Integer.parseInt(textTrackNumber);
        }catch (NumberFormatException nfe){
            JOptionPane.showMessageDialog(mainPanel, "Please enter a number as record number");
            return;
        }

        db.insertSong(songTitle,artistName,albumName,trackNumber);
        resetFields();
//        updateSongsTable();
    }

    private void resetFields() {
        titleTextField.setText("");
        trackTextField.setText("");
        albumTextField.setText("");
        artistNameTextField.setText("");
    }

    private void updateSongsTable() {
        Vector songs=db.queryAllSongs();
        tableModel.setDataVector(songs,columnNames);
    }

    private void updateAlbumsTable() {
        Vector albums = db.queryAllAlbums();

        tableModel.setDataVector(albums, columnNames);
    }

    private void updateArtistTable() {
        Vector artists = db.queryAllArtists();

        tableModel.setDataVector(artists, columnNames);
    }

}