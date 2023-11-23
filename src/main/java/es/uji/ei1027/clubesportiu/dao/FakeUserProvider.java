package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.enums.UjiType;
import es.uji.ei1027.clubesportiu.model.UjiParticipant;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FakeUserProvider implements UserLoginDao {

    private UjiParticipantDao ujiParticipantDao;
    @Autowired
    public void setUjiParticipantDao(UjiParticipantDao ujiParticipantDao) {
        this.ujiParticipantDao = ujiParticipantDao;
        generateFakeUsers();
    }
    private void generateFakeUsers() {
        // Creating the fake users
        BasicPasswordEncryptor crypt = new BasicPasswordEncryptor();
        UjiParticipant userStudentOne = new UjiParticipant();
        userStudentOne.setName("Pepe");
        userStudentOne.setEmailAddress("pepe@uji.es");
        userStudentOne.setPhoneNumber("640678909");
        userStudentOne.setAdmin(false);
        userStudentOne.setType(UjiType.STUDENT);
        userStudentOne.setPassword(crypt.encryptPassword("pepe"));

        UjiParticipant userStudentTwo = new UjiParticipant();
        userStudentTwo.setName("Eric");
        userStudentTwo.setEmailAddress("eric@uji.es");
        userStudentTwo.setPhoneNumber("654870967");
        userStudentTwo.setAdmin(false);
        userStudentTwo.setType(UjiType.STUDENT);
        userStudentTwo.setPassword(crypt.encryptPassword("eric"));

        UjiParticipant userStudentThree = new UjiParticipant();
        userStudentThree.setName("David");
        userStudentThree.setEmailAddress("david@uji.es");
        userStudentThree.setPhoneNumber("435096799");
        userStudentThree.setAdmin(false);
        userStudentThree.setType(UjiType.STUDENT);
        userStudentThree.setPassword(crypt.encryptPassword("david"));

        UjiParticipant userAdminOne = new UjiParticipant();
        userAdminOne.setName("Luis");
        userAdminOne.setEmailAddress("luis@uji.es");
        userAdminOne.setPhoneNumber("640987654");
        userAdminOne.setAdmin(true);
        userAdminOne.setType(UjiType.PAS);
        userAdminOne.setPassword(crypt.encryptPassword("luis"));

        UjiParticipant userAdminTwo = new UjiParticipant();
        userAdminTwo.setName("Andreu");
        userAdminTwo.setEmailAddress("andreu@uji.es");
        userAdminTwo.setPhoneNumber("123098567");
        userAdminTwo.setAdmin(true);
        userAdminTwo.setType(UjiType.PAS);
        userAdminTwo.setPassword(crypt.encryptPassword("andreu"));

        // Check if the fake users already exist
        if (ujiParticipantDao.getUjiParticipantByEmail(userStudentOne.getEmailAddress()) == null) {
            // Inserting the users to the database
            ujiParticipantDao.addUjiParticipant(userStudentOne);
            ujiParticipantDao.addUjiParticipant(userStudentTwo);
            ujiParticipantDao.addUjiParticipant(userStudentThree);
            ujiParticipantDao.addUjiParticipant(userAdminOne);
            ujiParticipantDao.addUjiParticipant(userAdminTwo);
        }
    }

    @Override
    public UjiParticipant loadUserByEmail(String email, String password) {
        UjiParticipant ujiParticipant = ujiParticipantDao.getUjiParticipantByEmail(email);
        if (ujiParticipant == null) {
            // User not found
            return null;
        }

        BasicPasswordEncryptor crypt = new BasicPasswordEncryptor();
        if (crypt.checkPassword(password, ujiParticipant.getPassword())) {
            return ujiParticipant;
        }

        // If reaches this point bad login
        return null;
    }
}
