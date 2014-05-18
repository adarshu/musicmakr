<?php
require('twilio-php/Services/Twilio.php');
require_once('lib/pubnub/Pubnub.php');

$sid = "AC502a93adcdf4f27c65b7deb23027f094"; // Your Account SID from www.twilio.com/user/account
$token = "4899636dd1ea3647709a78231da49b3d"; // Your Auth Token from www.twilio.com/user/account
$client = new Services_Twilio($sid, $token);

function startsWith($haystack, $needle)
{
    return !strncmp($haystack, $needle, strlen($needle));
}

// make an associative array of senders we know, indexed by phone number
$people = array(
    "+12223334444" => "Boots",
);

// if the sender is known, then greet them by name
// otherwise, consider them just another monkey
if (!$name = $people[$_REQUEST['From']]) {
    $name = "Person";
}

//logic
$resp = "default";
$phone = $_REQUEST['From'];
$body = $_REQUEST['Body'];

//if (startsWith(strtolower($body), "happy")) {
    $resp = "Added to playlist";
    $pubnub = new Pubnub('pub-c-f1c6575f-d2d3-4cd1-bb38-2492c50b7d0e', 'sub-c-e877ab56-de0c-11e3-8c07-02ee2ddab7fe', 'sec-c-YzFiZTI4OWUtZjFmMS00Y2IzLTg1ZTYtYWYwMDEwZDllNWRh', false, false);
    $info = $pubnub->publish(array(
        'channel' => 'hello_world', ## REQUIRED Channel to Send
        'message' => array("cmd" => "search", "val" => "$body") ## REQUIRED Message String/Array
    ));
//} else {
//    $resp = "Incorrect";
//}

//file_put_contents("reply.txt", $resp . "\n");

// now greet the sender
header("content-type: text/xml");
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
?>
<Response>
    <Message><?php echo $resp ?></Message>
</Response>
