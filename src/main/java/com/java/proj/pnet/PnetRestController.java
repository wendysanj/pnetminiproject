package com.java.proj.pnet;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/countrySEAsia")
public class PnetRestController {
    @Autowired
    private CountryRepository countryRepository;

    // @GetMapping("/countrySEAsia")
    @GetMapping
    public ResponseEntity<Object> getCountryList() {
        return new ResponseEntity(countryRepository.findAll(), HttpStatus.OK);
    }

    // @PostMapping("/countrySEAsia")
    @PostMapping
    public ResponseEntity<Object> saveCountry(@RequestParam final String user, @RequestBody final Country country) {
        System.out.println("saveCountry called, user=" + user);
        final String decrypString = DecryptDES.decrypt(user);

        if (decrypString == null) {
            return new ResponseEntity<>("unable decrypt url", HttpStatus.BAD_REQUEST);
        }

        try {
            // save customer input (json) in local directory
            final UUID id = UUID.randomUUID();

            Properties prop = new Properties();
            String propFileName = "application.properties";

            InputStream is = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (is != null) {
				prop.load(is);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            
             
            final String path = prop.getProperty("server.chksum.file");
            System.out.println("saveCountry called, path=" + path);
            //final String path = "E:\\";
            final GsonBuilder gsonMapBuilder = new GsonBuilder();
            final Gson gsonObject = gsonMapBuilder.create();
            final String fileData = gsonObject.toJson(country);
            final String fileDetail = path + decrypString + "~" + id + ".txt";
            final FileOutputStream fos = new FileOutputStream(fileDetail);
            System.out.println("\n\n... file created=" + fileDetail);
            fos.write(fileData.getBytes());
            fos.flush();
            fos.close();

            final String fileData2 = user;
            final String fileCheckSum = path + decrypString + "~" + id + "CHKSUM.txt";
            final FileOutputStream fos2 = new FileOutputStream(fileCheckSum);

            final InputStream inputStream = new ByteArrayInputStream(fileData2.getBytes());
            System.out.println("checksum result=" + getChecksumCRC32(inputStream, 10));

            fos2.write(fileData2.getBytes());
            fos2.flush();
            fos2.close();
            System.out.println("\n\n... file created=" + fileDetail);

        } catch (final Exception e) {
            System.out.println("file not created=" + e);
        }
        // VALIDATE SIMPLE CHECK MANDATORY

        if (country.getAlpha2Code() == null || country.getAlpha2Code().trim().equals("")) {
            System.out.println("Country ID is mandatory");
            return new ResponseEntity<>("Country ID is mandatory", HttpStatus.BAD_REQUEST);
        }

        if (country.getAlpha2Code().length() != 2) {
            System.out.println("Country ID incorrect length");
            return new ResponseEntity<>("Country ID incorrect length", HttpStatus.BAD_REQUEST);
        }

        // save data and return result
        countryRepository.save(country);
        System.out.println("\n\n... END saveCountry, return data");
        return new ResponseEntity<>(countryRepository.findAll(), HttpStatus.CREATED);
    }

    @PostMapping("/enc")
    public ResponseEntity<Object> doEncrypt(@RequestBody final Map<String, String> user) {
        System.out.println(
                "\n\n... doEncrypt=" + user.get("user") + ",result=" + DecryptDES.encrypt((String) user.get("user")));

        final Map<String, String> result = new HashMap<>();
        result.put("value", DecryptDES.encrypt((String) user.get("user")));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /*
     * @PostMapping("/enc") public ResponseEntity<Object> doEncrypt(@RequestBody
     * String user) { System.out.println("\n\n... doEncrypt="+user );
     * 
     * return new ResponseEntity<>(DecryptDES.encrypt(user), HttpStatus.OK); }
     */

    public ResponseEntity<Object> doDecrypt(@RequestParam final String user) {
        return new ResponseEntity<>(DecryptDES.decrypt(user), HttpStatus.OK);
    }

    @PostMapping("/chksum")
    public ResponseEntity<Object> dochksum(@RequestParam final String user) {
        final String path = "E:\\";
        final File fileChkSum = getLatestFilefromDir(path);
        System.out.println(fileChkSum);
        final String fileChkSumContent = readFileContent(fileChkSum);
        System.out.println(fileChkSumContent);
        String result = "";
        try {
            final Long l1 = getChecksumCRC32(new ByteArrayInputStream(fileChkSumContent.getBytes()), 10);
            final Long l2 = getChecksumCRC32(new ByteArrayInputStream(user.getBytes()), 10);
            System.out.println(l1);
            if (l1.equals(l2)) {
                result = readFileContent(new File(path+fileChkSum.getName().replace("CHKSUM", "")));
                System.out.println(result);
            } else {
                return new ResponseEntity<>(" NOT MATCH", HttpStatus.BAD_REQUEST);
            }


        } catch (final IOException e) {
         
            e.printStackTrace();
            return new ResponseEntity<>(" FAILURE", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static long getChecksumCRC32(final InputStream stream, final int bufferSize) throws IOException {
        final CheckedInputStream checkedInputStream = new CheckedInputStream(stream, new CRC32());
        final byte[] buffer = new byte[bufferSize];
        while (checkedInputStream.read(buffer, 0, buffer.length) >= 0) {
        }
        return checkedInputStream.getChecksum().getValue();
    }

    public static File getLatestFilefromDir(final String dirPath) {
        final File dir = new File(dirPath);
        final File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        File lastModifiedFile = files[0];

        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }

        try {

            final Scanner myReader = new Scanner(lastModifiedFile);
            while (myReader.hasNextLine()) {
                final String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (final FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return lastModifiedFile;
    }

    public static String readFileContent(final File file) {
        try {
            final Scanner myReader = new Scanner(file);
            String data = "";
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
            return data;
        } catch (final FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

}
