package com.BI183.karawila;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHendler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "db_beritaku";
    private final static String TABLE_BERITA = "t_berita";
    private final static String KEY_ID_BERITA = "ID_berita";
    private final static String KEY_JUDUL = "Judul";
    private final static String KEY_TGL = "Tanggal";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_CAPTION = "Caption";
    private final static String KEY_PENULIS = "Penulis";
    private final static String KEY_ISI_BERITA = "Isi_berita";
    private final static String KEY_LINK = "Link";
    private SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
    private Context context;


    public DatabaseHendler(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION );
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BERITA = "CREATE TABLE " + TABLE_BERITA
                + "(" + KEY_ID_BERITA + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUL + " TEXT, " + KEY_TGL + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_CAPTION + " TEXT,"
                + KEY_PENULIS + " TEXT, " + KEY_ISI_BERITA + " TEXT, "
                + KEY_LINK + " TEXT);";

        db.execSQL(CREATE_TABLE_BERITA);
        inisialisasiBeritaAwal(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_BERITA;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void tambahBerita(Berita dataBerita) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataBerita.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBerita.getTanggal()));
        cv.put(KEY_GAMBAR, dataBerita.getGambar());
        cv.put(KEY_CAPTION, dataBerita.getCaption());
        cv.put(KEY_PENULIS, dataBerita.getPenulis());
        cv.put(KEY_ISI_BERITA, dataBerita.getIsiBerita());
        cv.put(KEY_LINK, dataBerita.getLink());

        db.insert(TABLE_BERITA, null, cv);
        db.close();
    }

    public void tambahBerita(Berita dataBerita, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataBerita.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBerita.getTanggal()));
        cv.put(KEY_GAMBAR, dataBerita.getGambar());
        cv.put(KEY_CAPTION, dataBerita.getCaption());
        cv.put(KEY_PENULIS, dataBerita.getPenulis());
        cv.put(KEY_ISI_BERITA, dataBerita.getIsiBerita());
        cv.put(KEY_LINK, dataBerita.getLink());
        db.insert(TABLE_BERITA, null, cv);
    }

    public void editBerita(Berita dataBerita) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataBerita.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBerita.getTanggal()));
        cv.put(KEY_GAMBAR, dataBerita.getGambar());
        cv.put(KEY_CAPTION, dataBerita.getCaption());
        cv.put(KEY_PENULIS, dataBerita.getPenulis());
        cv.put(KEY_ISI_BERITA, dataBerita.getIsiBerita());
        cv.put(KEY_LINK, dataBerita.getLink());

        db.update(TABLE_BERITA, cv, KEY_ID_BERITA + "=?", new String[]{String.valueOf(dataBerita.getIdBerita())});
        db.close();
    }

    public void hapusBerita(int idBerita) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BERITA, KEY_ID_BERITA + "=?", new String[]{String.valueOf(idBerita)});
        db.close();
    }

    public ArrayList<Berita> getAllBerita() {
        ArrayList<Berita> dataBerita = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_BERITA;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if(csr.moveToFirst()) {
            do {
                Date tempDate = new Date();
                try {
                    tempDate = sdFormat.parse(csr.getString(2));
                } catch (ParseException er) {
                    er.printStackTrace();
                }

                Berita tempBerita = new Berita(
                       csr.getInt(0),
                       csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6),
                        csr.getString(7)
                );

                dataBerita.add(tempBerita);
            } while (csr.moveToNext());
        }

        return dataBerita;
    }

    private String storeImageFile(int id) {
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location =InputActivity.saveImageToInternalStorage(image, context);
        return location;
    }

    private void inisialisasiBeritaAwal(SQLiteDatabase db) {
        int idBerita = 0;
        Date tempDate = new Date();

        // Menambah databerita ke-1
        try {
            tempDate = sdFormat.parse("13/03/2020 06:22");
        }catch (ParseException er) {
            er.printStackTrace();
        }



        Berita berita1 = new Berita(
                idBerita,
                " Dongeng Daerah",
                tempDate,
                storeImageFile(R.drawable.a),
                "buku dongeng daerah",
                "Joko Dwinanto",
                "Isi Berita\tDi sebuah desa tinggalah seorang ibu bersama anak perempuannya yang bernama Darmi. Gadis itu memang rupawan, sayang sifatnya tak secantik wajahnya. Darmi adalah gadis pemalas yang hanya gemar bersolek. Setiap hari ia mematut dirinya di depan cermin, mengagumi kecantikan wajahnya.\n" +
                        "\n" +
                        "Ah, aku memang jelita, katanya. Lebih pantas bagiku untuk tinggal di istana raja daripada di gubuk reot seperti ini. Matanya memandang ke sekeliling ruangan. Hanya selembar kasur yang tidak empuk tempat dia tidur yang mengisi ruangan itu. Tidak ada meja hias yang sangat dia dambakan. Bahkan lemari untuk pakaian pun hanya sebuah peti bekas. Sampai kapan aku akan hidup seperti ini? keluh Darmi dalam hati.\n" +

                        "\n" +
                        "Darmi memang bukan anak orang kaya. Ayahnya sudah meninggal dan ibunya tak punya banyak uang. Untuk menghidupi mereka berdua, sang ibu bekerja membanting tulang dari pagi hingga malam. Pekerjaan apapun dia lakukan, mencari kayu bakar di hutan, menyabit rumput untuk pakan kambing tetangga, mencucikan pakaian orang lain. Pekerjaan apapun akan ia lakukan untuk memperoleh sedikit upah.\n",
                "https://dongengceritarakyat.com/legenda-cerita-batu-menangis-dongeng/"
        );

        tambahBerita(berita1, db);
        idBerita++;

        //Data berita ke-2
        try {
            tempDate = (sdFormat.parse("13/03/2020 06:15"));
        }catch (ParseException er) {
            er.printStackTrace();
        }
        Berita berita2 = new Berita(
                idBerita,
                "Malory Towers",
                tempDate,
                storeImageFile(R.drawable.b),
                "bercerita tentang pengalaman Darrell Rivers ketika bersekolah di Malory Towers",
                "Malory Towers",
                "\tYaitu Darrell Rivers, seorang remaja muda yang memiliki sifat ramah dan peduli, namun mudah sekali marah, terkadang sampai sulit untuk dikendalikan oleh dirinya dan orang lain. Sally Hope, seorang siswi yang bersifat pendiam dan sulit sekali untuk diajak berbicara, serta bersikap tertutup dan menjauhkan diri dari teman sekelasnya. Mary-Lou, perempuan yang penakut dan sensitif, ia sering dimanipulasi oleh Gwendoline namun ia tidak berani melawannya. Alicia Johns, perempuan yang iseng dan kasar, akibat memiliki banyak saudara laki-laki. Gwendoline Mary Lacey, seorang perempuan yang manja dan salah satu tokoh penyebab drama di dalam novel ini.\n" +
                        "\n" +
                        "\"Di dalam novel ini, siswi-siswi ditemani dan di didik oleh guru-guru mereka, Miss Potts dan Miss Grayling, serta guru mata pelajaran lainnya. Darrell Rivers adalah karakter utama di buku novel ini..\n" +
                        "\n" +
                        "\"Darrell Rivers adalah seorang siswi berumur dua belas tahun yang akan masuk ke sekolah barunya, yaitu Malory Towers. Di sana ia berhadapan dengan berbagai macam rintangan. Mulai dari teman sekelas iseng yang melibatkannya ke masalah, siswi yang selalu menyendiri, teman sekelas yang sangat membencinya, hingga disalahkan untuk sebuah perkara yang ia tidak lakukan. Di tambah lagi, Darrell adalah seseorang yang pemarah, dan hal tersebut malah membuat situasinya di sekolah bertambah parah. Bagaimana ia akan menyelesaikan masalah yang telah dilempar ke arahnya?\n",
                "https://www.kompasiana.com/kaiaiai127/5e81a376d541df01cf190f82/resensi-buku-malory-towers"
        );

        tambahBerita(berita2, db);
        idBerita++;

        //Data berita ke-3
        try {
            tempDate = sdFormat.parse("12/03/2020 22:46");
        }catch (ParseException er) {
            er.printStackTrace();
        }
        Berita berita3 = new Berita(
                idBerita,
                "Lupus",
                tempDate,
                storeImageFile(R.drawable.c),
                "anak muda gemar membaca dan menunggu setiap serialnya.",
                "Hilman Hariwijaya",
                "\tKenal Lupus? Anak kelas satu SMA Merah Putih yg doyan mengenakan baju lengan panjang itu? Dia lumayan ngetop loh!Serius. Kalau kebetulan kamu mampir kerumanya dan menyebut namanya,pasti orang seisi rumah pada tau semua. Itukan membuktikan bahwa dia cukup ngetop. Setidaknya,ya.... diantara orang seisi rumahnya. Model anaknya seperti kebanyakan remaja sekarang, kurus & rada tinggi. Tampangnya lumayanlah,daripada kejepit pintu. Yg menarik sih model rambut dgn rambut depan yg hampir menutup matanya. Sementara bagian samping dipotong rapi kearah belakang. Sedang bagian belakang, panjang hampir menutupi kerah biar kaya John Taylor sahutnya ge-er.\n" +
                        "\n" +
                        "Bila kamu kebetulan sempet memperhatikan dgn lebih seksama lagi, kamu akan melihat dia selalu membawa permen karet kemana dia pergi. Jangan sekali2 minta,karena dia terlalu pelit untuk memberikan makanan2 yg sangat dia sukai. Kecuali kalau kamu tukar dgn coklat yg harganya tentu lebih mahal. Dan lupus hanya akan memakan permen karetnya saat dia merasa grogi,bingung,atau tdk mempunyai makanan lain yg bisa dia minta dari temannya secara gratis. Curang ya?dia memang begitu. Dan satu hal yg jelek,dia tak pernah bisa menghilangkan kebiasaan buruknya untuk menempelkan bekas permen karet pada bangku sebelahnya yg kosong di bis kota. Entah berapa korban yg telah dirugikannya. Satu hal lagi yg perlu kamu ketahui, dia mempunyai sifat yg sangat pendiam. Terutama kalau lagi tidur. Tapi gk tentu juga. Dia bisa menjadi orang yg begitu cerewet bila berkumpul dgn orang2 yg disukainya.\n" +
                        "\n" +
                        "Dan seperti kebanyakan remaja lainnya, diapun amat menyukai musik. Semua musik kecuali musik ilustrasi film horor. Dia tak bisa melepaskan kebiasaanya untuk bernyanyi kalau lg jalan2 , kalau sudah begitu,teman sebelahnya akn terkejut dan menatap gemas padanya,\"kamu lagi batuk,ya?\"\n" +
                        "\n" +
                        "Dia juga suka menulis artikel dan kadang juga cerpen di majalah remaja. Keahlian ini mungkin satu2nya hal yg bisa dibanggakan dari dirinya. Karena dgn begitu, dia tak pernah minta uang dari ibunya kecuali kalau terpaksa.(malangnya dia justru sering berada dlm keadaan terpaksa harus minta uang pd ibunya). Tapi ibunya yg baik hati itu tak pernah kesal. Sebab kalu lagi punya uang banyak, Lupus sering memberikan sebagian kpd ibunya.\n" +

                        "\n" +

                        "\"Dan saat itu, Lupus masih asik berbengong-ria.Saking lamanya,muka udah kaya terminal face. Mana bawaanya lumayan banyak seperti orang yg mau pulang kampung.Ini gara2 guru biologi yg menyuruh bawa contoh2 tanaman,baju praktek,dan barang2 lain untuk praktekum biologi siang nanti.\n",
                "https://www.facebook.com/permalink.php?story_fbid=236440246712154&id=234959076860271&__tn__=K-R"
        );

        tambahBerita(berita3, db);
        idBerita++;

        //Data berita ke-4
        try {
            tempDate = sdFormat.parse("13/03/2020 05:58");
        }catch (ParseException er) {
            er.printStackTrace();
        }
        Berita berita4 = new Berita(
                idBerita,
                "Sapta Siaga",
                tempDate,
                storeImageFile(R.drawable.d),
                "cerita kali ini tidak bersekolah di asrama seperti buka karya lainnya.",
                "Enid Blyton",
                "\tBenny adalah seorang anak lelaki kecil pemalu berumur 8 tahun, dengan perawakan lebih kecil dari anak seusianya. Ia tidak bersekolah dan cenderung asosial. Benny adalah anak dari pasangan Tuan Luke Bolan dan Nyonya Bolan yang memiliki seorang lagi bayi perempuan, adik Benny.\n" +
                        "\n" +
                        "\"Tuan Luke bekerja sebagai tenaga pembantu di pasar malam yang selalu berpindah-pindah, sedangkan istrinya Nyonya Bolan sesekali ikut berjualan roti jahe di pasar malam apabila pasar malam kebetulan mampir dekat tempat tinggal mereka.\n" +

                        "\"Kalau tidak lagi berjualan roti jahe di pasar malam, sehari-harinya Nyonya Bolan membantu pekerjaan rumah orang-orang desa sambil membawa bayinya untuk mencari nafkah. Keluarga ini tinggal disebuah gubuk tua yang tidak nyaman, dingin dan berangin di sebuah lereng bukit, Hilly-Down, di Inggris.\n" +
                        "\n" +
                        "\"Pada suatu senja, saat Nyonya Bolan sambil membawa bayi perempuannya, berjualan roti jahe di arena pasar malam pada lembah di balik sisi lain bukit, gubuk tuanya terbakar. Begitu mendengar kejadiannya tentu saja ia sangat panik dan bergegas pulang, bukan karena takut harta bendanya terbakar, tetapi karena Benny ditinggal sendiri di rumah.\n" +
                        "\n" +
                        "Begitulah, Benny malang yang tidak bersekolah rupanya sering ditinggal sendiri di rumah. Benny yang perangainya tak wajar, akan segera bersembunyi setiap kali bertemu orang lain. Kondisinya membuatnya lebih sering ditinggal sendiri di rumah.\n",
                "https://www.kompasiana.com/teotarigan/5bf986bf6ddcae3272688212/sapta-siaga-misteri-biola-kuno?page=all"
        );

        tambahBerita(berita4, db);

    }
}
