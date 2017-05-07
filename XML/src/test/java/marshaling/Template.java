package marshaling;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ContactData")
@XmlAccessorType(XmlAccessType.FIELD)
public class Template {

    @XmlElement(name = "FirstName")
    private String firstName;

    @XmlElement(name = "LastName")
    private String lastName;

    private String gender;
    private String birthDate;
    private String phoneNumber;
    private String address;
    private String emailAddress;
    private String zipCode; //20002
    private String city; //WASHINGTON
    private String state; //DC

    public Template() {
    }

    public Template(String firstName, String lastName, String gender, String birthDate, String phoneNumber, String address, String emailAddress, String zipCode, String city, String state) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.emailAddress = emailAddress;
        this.zipCode = zipCode;
        this.city = city;
        this.state = state;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean equals(Template contact) {
        return contact != null && this.getFirstName().equals(contact.getFirstName())
                && this.getLastName().equals(contact.getLastName())
                && this.getZipCode().equals(contact.getZipCode())
                && this.getAddress().equals(contact.getAddress())
                && this.getEmailAddress().equals(contact.getEmailAddress())
                && this.getPhoneNumber().equals(contact.getPhoneNumber());
    }

    @Override
    public String toString() {
        return //"\n\n-----------Contact ----- DATA --------------- " +
                "\nfirstName='" + firstName + '\'' +
                        ",\nlastName='" + lastName + '\'' +
                        ",\ngender='" + gender + '\'' +
                        ",\nbirthDate='" + birthDate + '\'' +
                        ",\nphoneNumber='" + phoneNumber + '\'' +
                        ",\naddress='" + address + '\'' +
                        ",\nemailAddress='" + emailAddress + '\'' +
                        ",\nzipCode='" + zipCode + '\'' +
                        ",\ncity='" + city + '\'' +
                        ",\nstate='" + state + '\'';
    }
}
