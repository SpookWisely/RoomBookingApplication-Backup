<?php
require_once ("DatabaseConnector.php");

$response = array();

//Using isset in the if statement allows to check if the response given is that of an
//API call so it can tell to ignore it or not.
if(isset($_GET['apicall'])) {
    //Switch to take the value from the apiRequest variable and then compares it to the two

    switch ($_GET['apicall']) {

        //Switch Case for logging in

        case 'LoggingIn':

            if(paramChecker(array("userName","password"))) {

                $userName = $_POST['userName'];
                $password = md5($_POST['password']);

                $sqlStatement = $connection->prepare("Select UserID, fullName,userName,email ,AdminCheck ,Management FROM users WHERE UserName = ? && password = ?");
                $sqlStatement->bind_param("ss",$userName,$password);

                $sqlStatement->execute();

                $sqlStatement->store_result();
                //If sql statement above returns a row containing the username and
                //password provided enter the if statement else user doesn't exist.
                if($sqlStatement->num_rows > 0) {

                    $sqlStatement->bind_result($UserID, $fullName, $userName, $email, $AdminCheck,$Management);
                    //fetch code preps the results from the above code to be stored within the array below.
                    $sqlStatement->fetch();

                    $user = array(
                        'userid'=>$UserID,
                        'FullName'=>$fullName,
                        'username'=>$userName,
                        'email'=>$email,
                        'adminCheck' =>$AdminCheck,
                        'Management' =>$Management

                    );
                    $response['error'] = false;
                    $response['message'] = 'Login Successful';
                    $response['user'] = $user;

                }else {
                    $response['error'] = true;
                    $response['message'] = 'Invalid Username or Password';
                }
            }
            break;

        //Switch Case for signing up

        case 'SigningUp':
            //Make use of the validation Function to check
            ob_start();
          // var_dump(paramChecker(array('fullName','userName','email','password')));
           $result = ob_get_clean();
            if(paramChecker(array("fullName","userName","email","password"))) {

                $fullName = $_POST['fullName'];
                $userName = $_POST['userName'];
                $email = $_POST['email'];
                //Password variable that's been salted to prevent rainbow tables being used
                //to break encryption.
                $password = md5($_POST['password']);


                
                $sqlStatement = $connection->prepare("Select UserID FROM users WHERE UserName = ? OR Email = ?");
                $sqlStatement->bind_param("ss", $userName, $email);
                $sqlStatement->execute();
                $sqlStatement->store_result();
                //Checks if the sql query calls back any rows

                if ($sqlStatement->num_rows > 0) {
                    $response['error'] = true;
                    $response['message'] = 'User already registered';
                    $sqlStatement->close();
                } else {

                    $sqlStatement = $connection->prepare("INSERT INTO users(FullName,UserName,Email,Password) VALUES(?,?,?,?)");
                    $sqlStatement ->bind_param("ssss", $fullName, $userName, $email, $password);

                    if ($sqlStatement->execute()) {

                        $sqlStatement = $connection->prepare("SELECT UserID, UserID, FullName,UserName,Email,AdminCheck,Management FROM users where UserName =?");
                        $sqlStatement->bind_param("s", $userName);
                        $sqlStatement->execute();
                        $sqlStatement->bind_result($userID, $UserID, $fullName, $userName, $email,$AdminCheck,$Management);
                        $sqlStatement->fetch();

                        $user = array(
                            'userID'=>$UserID,
                            'FullName'=>$fullName,
                            'username'=>$userName,
                            'email'=>$email,
                            'adminChecker'=>$AdminCheck,
                            'management' =>$Management
                        );

                        $sqlStatement->close();

                        $response['error'] = false;
                        $response['message'] = 'User registered successfully';
                        $response['user'] = $user;
                    }
                }
            }else {
                $response['error'] = true;
                $response['message'] = 'parameters missing';

            }
            break;


        case 'SectionCreation':
            ob_start();
            //Cleans the input stream
            $result = ob_clean();
            if(paramChecker(array("Section_RoomName","TotalCapacity","IsRoom","IsSection"))) {

                $isRoom = $_POST["IsRoom"];
                $isSection = $_POST["IsSection"];
                $section_RoomName = $_POST['Section_RoomName'];
                $totalCapacity =  $_POST['TotalCapacity'];

                $sqlStatement = $connection->prepare("SELECT SectionID FROM sections_rooms WHERE Section_RoomName = ?");
                $sqlStatement->bind_param("s", $section_RoomName);
                $sqlStatement->execute();
                $sqlStatement->store_result();
                //Checks if the sql query calls back any rows

                if ($sqlStatement->num_rows > 0) {
                    $response['error'] = true;
                    $response['message'] = 'Section/Room already exists ';
                    $sqlStatement->close();

                } else {

                    $sqlStatement = $connection->prepare("INSERT INTO sections_rooms(Section_RoomName,TotalCapacity,IsRoom,IsSection) VALUES(?,?,?,?)");
                    $sqlStatement ->bind_param("ssii", $section_RoomName,$totalCapacity, $isRoom,$isSection );

                    if ($sqlStatement->execute()) {

                        $sqlStatement = $connection->prepare("SELECT SectionID,Section_RoomName,TotalCapacity,IsRoom,IsSection FROM sections_rooms where Section_RoomName =?");
                        $sqlStatement->bind_param("s", $section_RoomName);
                        $sqlStatement->execute();
                        $sqlStatement->bind_result($sectionID, $section_RoomName, $totalCapacity,$isRoom,$isSection);
                        $sqlStatement->fetch();

                        $Section = array(
                            'sectionID'=>$sectionID,
                            'Section_RoomName'=>$section_RoomName,
                            'TotalCapacity'=>$totalCapacity,
                            'isRoom'=>$isRoom,
                            'isSection'=>$isSection
                        );

                        $sqlStatement->close();

                        $response['error'] = false;
                        $response['message'] = 'Section/Room Successfully Added to Database';
                        $response['section'] = $Section;
                    }

                }
            } else {
                $response['error'] = true;
                $response['message'] = 'parameters missing';

            }
                break;
        case 'SectionDeletion':
            ob_start();
            //Cleans the
            $result = ob_clean();
            if(paramChecker(array("SectionID"))) {

                $sectionID = $_POST['SectionID'];

                $sqlStatement = $connection->prepare("DELETE FROM sections_rooms WHERE SectionID = ?");
                $sqlStatement->bind_param("s",$sectionID);
                $sqlStatement->execute();
                if($connection->affected_rows > 0) {
                    $sqlStatement->close();
                    $response ['error'] = false;
                    $response ['message'] = "Specified Section all associated Bookings have been Deleted";
                } else {
                    $sqlStatement->close();
                    $response ['error'] = true;
                    $response ['message'] = "Query has executed but no changes were made.";
                }

            } else{
                $response ['error'] = true;
                $response ['message'] = "Invalid Parameters Inputted";
            }

            break;
        case 'SectionCall':
            $sqlStatement = "SELECT * FROM sections_rooms WHERE isSection = 1";
            $result = mysqli_query($connection, $sqlStatement);
            $sections = array();
            if ($row = mysqli_num_rows($result) > 0) {
                while($row = mysqli_fetch_assoc($result)) {
                    $sections[] = $row;
                }
                print(json_encode($sections));
            }
           /* foreach ($Sections as $section) {
                echo $section['SectionID'];
            }*/


           // $response['message'] = 'Here are all Sections ';
          // $response['Sections'] = $Sections;

            break;

        case 'RoomCall':
            $sqlStatement = "SELECT * FROM sections_rooms Where isRoom = 1 ";
            $result = mysqli_query($connection, $sqlStatement);
            $sections = array();
            if ($row = mysqli_num_rows($result) > 0) {
                while($row = mysqli_fetch_assoc($result)) {
                    $sections[] = $row;
                }
                print(json_encode($sections));
            }

            break;

        case 'AllSectionRoomCall':
            $sqlStatement = "SELECT * FROM sections_rooms ";
            $result = mysqli_query($connection, $sqlStatement);
            $sections = array();
            if ($row = mysqli_num_rows($result) > 0) {
                while($row = mysqli_fetch_assoc($result)) {
                    $sections[] = $row;
                }
                print(json_encode($sections));
            }

            break;
            case 'BookingCreation':
                if(paramChecker(array("UserID","SectionID","SeatAmount","DateOfBooking","BookingStartTime","BookingEndTime"))) {

                    $UserID = $_POST['UserID'];
                    $SectionID = $_POST['SectionID'];
                    $SeatAmount = $_POST['SeatAmount'];
                    $DateOfBooking = Date("Y-m-d",strtotime($_POST['DateOfBooking']));
                    $BookingStartTime =  Date("H:i",strtotime($_POST['BookingStartTime']));
                    $BookingEndTime =  Date("H:i",strtotime($_POST['BookingEndTime']));

                    $sqlStatement = $connection->prepare("SELECT BookingID FROM bookings WHERE SectionID = ? && DateOfBooking = ? && BookingStartTime = ?");
                    $sqlStatement->bind_param("sss", $SectionID, $DateOfBooking, $BookingStartTime);
                    $sqlStatement->execute();
                    $sqlStatement->store_result();


                    if ($sqlStatement->num_rows > 0) {
                        $sqlStatement = $connection->prepare("Select SectionID, DateOfBooking, BookingStartTime, SUM(SeatAmount) from bookings GROUP BY SectionID, DateOfBooking, BookingStartTime 
                        HAVING SectionID = ? AND DateOfBooking = ? AND BookingStartTime = ? AND SUM(seatAmount) >= (SELECT TOTALCAPACITY from sections_rooms WHERE SectionID = ?)");
                        $sqlStatement->bind_param("issi", $SectionID, $DateOfBooking, $BookingStartTime,$SectionID);
                        $sqlStatement->execute();
                        $sqlStatement->store_result();

                        if($sqlStatement->num_rows > 0) {

                            $response['error'] = true;
                            $response['message'] = 'There are no booking slots available for given time and date';
                            $sqlStatement->close();

                        }else {


                            $sqlStatement = $connection->prepare("INSERT INTO bookings(UserID,SectionID,SeatAmount,DateOfBooking,BookingStartTime,BookingEndTime) values (?,?,?,?,?,?)");
                            $sqlStatement->bind_param("iiisss", $UserID,$SectionID, $SeatAmount, $DateOfBooking, $BookingStartTime, $BookingEndTime);

                            if ($sqlStatement->execute()) {
                                $response['message'] = 'Before Booking Selection ';
                                $sqlStatement = $connection->prepare("SELECT * FROM bookings ORDER by CreationTime ASC ");
                                $sqlStatement->execute();
                                $sqlStatement->bind_result($BookingID, $UserID, $SectionID, $SeatAmount, $CreationDate, $CreationTime, $DateOfBooking, $BookingStartTime, $BookingEndTime);
                                $sqlStatement->fetch();

                                $Booking = array(
                                    'BookingID' =>$BookingID,
                                    'UserID' =>$UserID,
                                    'SectionID' =>$SectionID,
                                    'SeatAmount' =>$SeatAmount,
                                    'CreationDate' =>$CreationDate,
                                    'CreationTime' =>$CreationTime,
                                    'DateOfBooking' =>$DateOfBooking,
                                    'BookingStartTime' =>$BookingStartTime,
                                    'BookingEndTime' =>$BookingEndTime
                                );
                                $sqlStatement->close();

                                $response['error'] = false;
                                $response['message'] = 'Booking Added Successfully';
                                $response['booking'] = $Booking;
                            } else {
                                $response['error'] = true;
                                $response['message'] = 'SQL not Executed';

                            }
                        }
                    } else {

                        $sqlStatement = $connection->prepare("SELECT TotalCapacity from sections_rooms WHERE SectionID = ?");
                        $sqlStatement->bind_param("i" ,$SectionID);
                        $sqlStatement->execute();
                        $sqlStatement->bind_result($totalcap);
                        $sqlStatement->fetch();
                        if($SeatAmount <= $totalcap) {
                        $sqlStatement->reset();
                        $sqlStatement = $connection->prepare("Select SectionID, DateOfBooking, BookingStartTime, SUM(SeatAmount) from bookings GROUP BY SectionID, DateOfBooking, BookingStartTime 
                        HAVING SectionID = ? AND DateOfBooking = ? AND BookingStartTime = ? AND SUM(seatAmount) >= (SELECT TOTALCAPACITY from sections_rooms WHERE SectionID = ?)");
                        $sqlStatement->bind_param("issi", $SectionID, $DateOfBooking, $BookingStartTime, $SectionID);
                        $sqlStatement->execute();
                        $sqlStatement->store_result();

                        if ($sqlStatement->num_rows > 0) {

                            $response['error'] = true;
                            $response['message'] = 'There are no booking slots available for given time and date';
                            $sqlStatement->close();
                        } else {
                            $sqlStatement = $connection->prepare("INSERT INTO bookings(UserID,SectionID,SeatAmount,DateOfBooking,BookingStartTime,BookingEndTime) values (?,?,?,?,?,?)");
                            $sqlStatement->bind_param("iiisss", $UserID, $SectionID, $SeatAmount, $DateOfBooking, $BookingStartTime, $BookingEndTime);

                            if ($sqlStatement->execute()) {
                                $response['message'] = 'Before Booking Selection ';
                                $sqlStatement = $connection->prepare("SELECT * FROM bookings ORDER by BookingID ASC ");
                                $sqlStatement->execute();
                                $sqlStatement->bind_result($BookingID, $UserID, $SectionID, $SeatAmount, $CreationDate, $CreationTime, $DateOfBooking, $BookingStartTime, $BookingEndTime);
                                $sqlStatement->fetch();

                                $Booking = array(
                                    'BookingID' => $BookingID,
                                    'UserID' => $UserID,
                                    'SectionID' => $SectionID,
                                    'SeatAmount' => $SeatAmount,
                                    'CreationDate' => $CreationDate,
                                    'CreationTime' => $CreationTime,
                                    'DateOfBooking' => $DateOfBooking,
                                    'BookingStartTime' => $BookingStartTime,
                                    'BookingEndTime' => $BookingEndTime
                                );
                                $sqlStatement->close();

                                $response['error'] = false;
                                $response['message'] = 'Booking Added Successfully';
                                $response['booking'] = $Booking;
                            } else {
                                $response['error'] = true;
                                $response['message'] = 'SQL not Executed';
                            }
                        }

                    } else {
                            $response['error'] = true;
                            $response['message'] = 'Seats Requested is larger than totalcapacity ';

                        }
                    }

                } else {
                    $response['error'] = true;
                    $response['message'] = 'Invalid Booking Parameter';
                }
            break;

            case 'BookingDeletion':
            if(paramChecker(array("BookingID"))) {
                $bookingID = $_POST["BookingID"];


                $sqlStatement = $connection->prepare("DELETE FROM bookings WHERE BookingID = ?");
                $sqlStatement->bind_param("s",$bookingID);
                $sqlStatement->execute();

                if($connection->affected_rows > 0) {
                    $sqlStatement->close();
                    $response ['error'] = false;
                    $response ['message'] = "Specified Booking has been Deleted";

                } else {
                    $sqlStatement->close();
                    $response ['error'] = false;
                    $response ['message'] = "Query has executed by no changes were made.";
                }

            } else{
                $response ['error'] = true;
                $response ['message'] = "Invalid Parameters Inputted";
            }
            break;

        case 'GetSeats':
            if(paramChecker(array("Section_RoomName"))) {
            $section_RoomName = $_POST["Section_RoomName"];

            $sqlStatement = $connection->prepare("SELECT * FROM sections_Rooms WHERE Section_Roomname = ?");
            $sqlStatement->bind_param("s",$section_RoomName);
            $sqlStatement->execute();
            $sqlStatement->bind_result($SectionID,$Section_RoomName,$totalCapacity,$isRoom,$isSection);
            $sqlStatement->fetch();

            $Section = array (
                'SectionID' => $SectionID,
                'Section_RoomName' => $Section_RoomName,
                'TotalCapacity'=>$totalCapacity,
                'isRoom'=>$isRoom,
                'isSection'=>$isSection

            );

                $sqlStatement->close();
                $response ['error'] = false;
                $response ['message'] = "Section with specified with name";
                $response ['Section'] =  $Section;

            } else {
                $response ['error'] = true;
                $response ['message'] = "Invalid Parameters Inputted";
            }
            break;


        case 'GetCurrentSeats':
            if(paramChecker(array("Section_RoomName","DateOfBooking","BookingStartTime"))) {

                $section_RoomName = $_POST["Section_RoomName"];
               // $SeatAmount = $_POST['SeatAmount'];
                $DateOfBooking = Date("Y-m-d",strtotime($_POST['DateOfBooking']));
                $BookingStartTime =  Date("H:i",strtotime($_POST['BookingStartTime']));


                $sqlStatement = $connection->prepare("SELECT SectionID,Section_RoomName,TotalCapacity from sections_rooms WHERE Section_RoomName = ?");
                $sqlStatement->bind_param("s", $section_RoomName);
                $sqlStatement->execute();
                $sqlStatement->bind_result($sectionID,$Section_RoomName,$totalCapacity);
                $sqlStatement->fetch();

                //echo $sectionID;
                //echo "\n";
                //echo $section_RoomName;
                //echo "\n";
                //echo $totalCapacity;
                //echo "\n";

                $sqlStatement->reset();
                $sqlStatement = $connection->prepare( "SELECT SUM(SeatAmount) FROM bookings GROUP BY SectionID,DateOfBooking,BookingStartTime HAVING SectionID = ? AND DateOfBooking = ? AND BookingStartTime = ?");
                $sqlStatement->bind_param("iss",$sectionID,$DateOfBooking,$BookingStartTime);
                $sqlStatement->execute();
                $sqlStatement->bind_result($currentseats);
                $sqlStatement->fetch();
                $currentCap = $totalCapacity - $currentseats;


                $Section = array (
                    'SectionID' => $sectionID,
                    'Section_RoomName' => $Section_RoomName,
                    'TotalCapacity'=>$totalCapacity,
                    'CurrentCapacity' =>$currentCap
                );

                $sqlStatement->close();
                $response ['error'] = false;
                $response ['message'] = "Section with specified with name";
                $response ['Section'] =  $Section;

            } else {
                $response ['error'] = true;
                $response ['message'] = "Invalid Parameters Inputted";
            }
            break;



        case 'GetBookings':
            $sqlStatement = "SELECT * FROM bookings";
            $result = mysqli_query($connection, $sqlStatement);
            $bookings = array();
            if ($row = mysqli_num_rows($result) > 0) {
                while($row = mysqli_fetch_assoc($result)) {
                    $bookings[] = $row;
                }
                print(json_encode($bookings));
            }
            break;

        case 'GetUsers':

            $sqlStatement = "SELECT * FROM users";
            $result = mysqli_query($connection, $sqlStatement);
            $users = array();
            if ($row = mysqli_num_rows($result) > 0) {
                while($row = mysqli_fetch_assoc($result)) {
                    $users[] = $row;
                }
                print(json_encode($users));
            }
            break;

        case 'GetUserBookings':
            if(paramChecker(array("UserID"))) {
                $userID = $_POST["UserID"];

                $sqlStatement = "SELECT BookingID, UserID,SectionID,SeatAmount,DateOfBooking,BookingStartTime,BookingEndTime FROM bookings WHERE UserID = $userID";
                //$sqlStatement->bind_param("s", $userID);
                $result = mysqli_query($connection, $sqlStatement);
                $bookings= array();
                if ($row = mysqli_num_rows($result) > 0) {
                    while($row = mysqli_fetch_assoc($result)) {
                        $bookings[] = $row;
                    }
                    print(json_encode($bookings));
                }

                //$sqlStatement->close();
                $response ['error'] = false;
                $response ['message'] = "Bookings Under Specified UserID";
                $response ['UserBookings'] =  $bookings;

            } else {
                $response ['error'] = true;
                $response ['message'] = "Invalid Parameters Inputted";
            }
            break;

        case 'UserDeletion':
                if(paramChecker(array("UserID"))) {
                    $userID = $_POST["UserID"];

                    $sqlStatement = $connection->prepare("DELETE FROM users WHERE UserID = ?");
                    $sqlStatement->bind_param("s",$userID);
                    $sqlStatement->execute();

                    if($connection->affected_rows > 0) {
                        $sqlStatement->close();
                        $response ['error'] = false;
                        $response ['message'] = "Specified User all associated Bookings have been Deleted";

                    } else {
                        $sqlStatement->close();
                        $response ['error'] = false;
                        $response ['message'] = "Query has executed by no changes were made.";
                    }

                } else {
                    $response ['error'] = true;
                    $response ['message'] = "Invalid Parameters Inputted";
                }

            break;

        case 'EditAdmin':
            if(paramChecker(array("UserID"))) {
            $userID = $_POST["UserID"];

            $sqlStatement = $connection->prepare("SELECT * FROM users WHERE UserID = ?");
            $sqlStatement->bind_param("s", $userID);
            $sqlStatement->execute();
            $sqlStatement->store_result();
            //Checks to see if their is a valid UserID is given and no duplicates
            if ($sqlStatement->num_rows > 0 || $sqlStatement->num_rows < 2) {

                $sqlStatement = $connection->prepare("SELECT * FROM users WHERE UserID = ? && AdminCheck = 0");
                $sqlStatement->bind_param("s", $userID);
                $sqlStatement->execute();
                $sqlStatement->store_result();

                //If the supplied UserID has a value of 0 in "AdminCheck" Column it will then change it to a 1 and if it doesn't find it with a value of 0 it
                //will assume its value is 1 and change it back to 0.
                if($sqlStatement->num_rows > 0) {

                    $sqlStatement = $connection->prepare("UPDATE users SET AdminCheck = 1 WHERE UserID = ?");
                    $sqlStatement->bind_param("s", $userID);
                    $sqlStatement->execute();

                    $response ['error'] = false;
                    $response ['message'] = "User AdminCheck Account Value was set to 1";
                    $sqlStatement->close();
                } else {
                    $sqlStatement = $connection->prepare("UPDATE users SET AdminCheck = 0 WHERE UserID = ?");
                    $sqlStatement->bind_param("s", $userID);
                    $sqlStatement->execute();

                    $response ['error'] = false;
                    $response ['message'] = "User AdminCheck Account Value was set to 0";
                    $sqlStatement->close();

                }
            } else {
                $response['error'] = true;
                $response['message'] = 'UserID does not exist';
                $sqlStatement->close();
            }
    } else {
        $response ['error'] = true;
        $response ['message'] = "Invalid Parameters Inputted";
    }
    break;
        case 'EditManagement':
            if(paramChecker(array("UserID"))) {
                $userID = $_POST["UserID"];

                $sqlStatement = $connection->prepare("SELECT * FROM users WHERE UserID = ?");
                $sqlStatement->bind_param("s", $userID);
                $sqlStatement->execute();
                $sqlStatement->store_result();
                //Checks to see if their is a valid UserID is given and no duplicates
                if ($sqlStatement->num_rows > 0 || $sqlStatement->num_rows < 2) {

                    $sqlStatement = $connection->prepare("SELECT * FROM users WHERE UserID = ? && Management = 0");
                    $sqlStatement->bind_param("s", $userID);
                    $sqlStatement->execute();
                    $sqlStatement->store_result();

                    //If the supplied UserID has a value of 0 in "Management" Column it will then change it to a 1 and if it doesn't find it with a value of 0 it
                    //will assume its value is 1 and change it back to 0.
                    if($sqlStatement->num_rows > 0) {

                        $sqlStatement = $connection->prepare("UPDATE users SET Management = 1 WHERE UserID = ?");
                        $sqlStatement->bind_param("s", $userID);
                        $sqlStatement->execute();

                        $response ['error'] = false;
                        $response ['message'] = "User Management Account Value was set to 1";
                        $sqlStatement->close();
                    } else {
                        $sqlStatement = $connection->prepare("UPDATE users SET Management = 0 WHERE UserID = ?");
                        $sqlStatement->bind_param("s", $userID);
                        $sqlStatement->execute();

                        $response ['error'] = false;
                        $response ['message'] = "User Management Account Value was set to 0";
                        $sqlStatement->close();

                    }
                } else {
                    $response['error'] = true;
                    $response['message'] = 'UserID does not exist';
                    $sqlStatement->close();
                }
            } else {
                $response ['error'] = true;
                $response ['message'] = "Invalid Parameters Inputted";
            }
            break;

        case 'CountSections':

            $sqlStatement = "SELECT SectionID, count(*) FROM bookings GROUP by SectionID ORDER by count(SectionID) DESC";
            $result = mysqli_query($connection, $sqlStatement);
            $sectionID= array();
            if ($row = mysqli_num_rows($result) > 0) {
                while($row = mysqli_fetch_assoc($result)) {
                    $sectionID[] = $row;
                }
                print(json_encode($sectionID));
            }

            break;

        case 'RecoverDetails':
            if(paramChecker(array("Email"))) {
                $email = $_POST["Email"];

                $sqlStatement = $connection->prepare("SELECT * FROM users WHERE  Email = ?");
                $sqlStatement->bind_param("s", $email);
                $sqlStatement->execute();
                $sqlStatement->store_result();


                //Checks to see if their is a valid email is given it exists
                if ($sqlStatement->num_rows > 0) {

                    $sqlStatement = $connection->prepare("SELECT UserName,Email,Password FROM users WHERE Email = ?");
                    $sqlStatement->bind_param("s", $email);
                    $sqlStatement->execute();
                    $sqlStatement->bind_result($userName,$Email,$password);
                    $sqlStatement->fetch();

                    /*$header = "From: bookittesting2021@gmail.com\r\n";
                    $header.= "MIME-Version: 1.0\r\n";
                    $header.= "Content-Type: text/html; charset=ISO-8859-1\r\n";
                    $header.= "X-Priority: 1\r\n";*/


                    $msg = "Your Details are as follows \n +  UserName: $userName + \n + Email: $Email + \n + Password:  + $password";

                    ini_set("SMTP","smtp.gmail.com");
                    ini_set("smtp_port","587");
                    ini_set("sendmail_from","bookittesting2021@gmail.com");
                    ini_set("sendmail_path", "D:\Xampp\Install\sendmail.exe -t -i");
                    mail($Email, "Account Details", $msg);

                    if(mail($email, "Account Details", $msg)) {
                        $sqlStatement->close();

                        $response ['error'] = false;
                        $response ['message'] = "User has been found and details have been emailed to relevant email address!";

                    }else {

                        $response ['error'] = true;
                        $response ['message'] = "Email did not send";
                    }

                } else {
                    $response ['error'] = true;
                    $response ['message'] = "No User with provided Email could be found!";
                    $response ['Email'] = $email;
                    $sqlStatement = $connection->prepare("SELECT UserName,Email,Password FROM users WHERE Email = ?");
                    $sqlStatement->bind_param("s", $email);
                    $sqlStatement->execute();
                    $sqlStatement->bind_result($userName,$Email,$password);

                    $response ['results'] = "username = $userName  email =$Email password = $password";
                }
            } else {
                $response ['error'] = true;
                $response ['message'] = "Invalid Parameters Inputted";
            }

            break;

            //If none of the cases are met it defaults to an error message
        default:
            $response['failure'] = true;

                $response['response'] = 'Unknown Operation Called';

    }
    } else {
        $response['failure'] = true;

        $response['response'] = 'Unknown API Call';
}
    //"echo" is used to output parameters with the extension of "json_encode this allows
    //the parameters to be pushed out to be encoded in a "json" file.
    echo json_encode($response);

    //validation of parameters
    function paramChecker($parameters) {

        //For each is essentially a loop that goes over all values provided
        //in this case "$parameters"
    foreach($parameters as $parameter) {

        if(!isset($_POST[$parameter])){

            return false;
        }
    }
    return true;

    }

