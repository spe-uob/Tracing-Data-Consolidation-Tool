import logo from './logo.svg';
import './App.css';
import UploadFile from './Uploadhandler/UploadFile';
import ServeFile from './ServeHandler/ServeFile';

function App() {
  return (
    <div className="App">
      <UploadFile></UploadFile>
      <ServeFile></ServeFile>
    </div>
  );
}

export default App;
