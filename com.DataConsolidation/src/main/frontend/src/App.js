import logo from './logo.svg';
import './App.css';
import UploadFile from './Uploadhandler/UploadFile';
import Knitform from './KnitHandler/Knitform'

function App() {
  return (
    <div className="App">
      <UploadFile></UploadFile>
      <Knitform></Knitform>
    </div>
  );
}

export default App;
