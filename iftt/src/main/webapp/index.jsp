<html>
<body>
<h2>Hello World!</h2>

<form action="login" method="POST">
	<input type=text name="username" placeholder="Username"> 
	<input type=password name=password placeholder="Password">    
    <p><button type="submit">Login app</button></p>
</form>

<form action="logout" method="POST">  
    <p><button type="submit">Logout app</button></p>
</form>

<!-- GOOGLE -->

<form action="connect/google" method="POST">
    <p>You haven't created any connections with Twitter yet. Click the button to create
       a connection between your account and your Twitter profile.
       (You'll be redirected to Twitter where you'll be asked to authorize the connection.)</p>
    <input type="hidden" name="scope" value="https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/calendar https://mail.google.com/" />
    <p><button type="submit">Connect Google</button></p>
</form>


<form action="connect/google" method="post">
  <div class="formInfo">
    <p>
      Spring Social Showcase is connected to your Twitter account.
      Click the button if you wish to disconnect.
    </p>
  </div>
   <input type="hidden" name="scope" value="https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/calendar https://mail.google.com/" />
  <button type="submit">Disconnect Google</button>
  <input type="hidden" name="_method" value="delete" />
</form>

<!-- TWITTER -->

<form action="connect/twitter" method="POST">
    <p>You haven't created any connections with Twitter yet. Click the button to create
       a connection between your account and your Twitter profile.
       (You'll be redirected to Twitter where you'll be asked to authorize the connection.)</p>
    <p><button type="submit">Connect Twitter</button></p>
</form>


<form action="connect/twitter" method="post">
  <div class="formInfo">
    <p>
      Spring Social Showcase is connected to your Twitter account.
      Click the button if you wish to disconnect.
    </p>
  </div>
  <button type="submit">Disconnect Twitter</button>
  <input type="hidden" name="_method" value="delete" />
</form>


</body>
</html>
