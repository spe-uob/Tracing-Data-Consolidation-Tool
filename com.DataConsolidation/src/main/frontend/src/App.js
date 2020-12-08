import logo from './logo.svg';
import './App.css';
import axios from "axios";
import { useEffect } from 'react';


const HelloComponent = () => {   //Create a Hello React Component
  const fetchHelloComponent = () => {
    axios.get("http://localhost:8080/hello").then(res => {console.log(res)});  //connect to our backend at the give URL
  }

  useEffect(() => {
    fetchHelloComponent();
  }, [])

  return <h1>Hello</h1>   //our GUI written in JS with React will be returned here. just Hello for now.
}



function App() {
  return (
    <div className="App"><HelloComponent/>
    </div>
  );
}

export default App;
