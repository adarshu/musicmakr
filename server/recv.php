<?php
require('twilio-php/Services/Twilio.php');

$sid = "AC19d4b688c588b4a5f631976d8ac62a64"; // Your Account SID from www.twilio.com/user/account
$token = "6d42cd04bbbdc0dc2580b59ac99aff95"; // Your Auth Token from www.twilio.com/user/account
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

if (startsWith(strtolower($body), "happy")) {
    $resp = "Added to playlist";
} else {
    $resp = "Incorrect";
}

file_put_contents("reply.txt", $resp . "\n");

// now greet the sender
header("content-type: text/xml");
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
?>
<Response>
    <Message><?php echo $resp ?></Message>
</Response>
